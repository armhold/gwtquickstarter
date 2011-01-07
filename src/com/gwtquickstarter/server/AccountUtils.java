package com.gwtquickstarter.server;

import java.security.*;
import java.util.Arrays;
import com.gwtquickstarter.client.util.*;
import com.google.appengine.repackaged.com.google.common.util.*;

/**
 * largely inspired by the excellent article at http://www.owasp.org/index.php/Hashing_Java
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class AccountUtils
{
    private final static int ITERATION_NUMBER = 1000;

    /**
     * Authenticates the user with a given username and password
     * If password and/or username is null then always returns false.
     * If the user does not exist in the database returns false.
     *
     * @param username    String The username of the user
     * @param password String The password of the user
     * @return boolean Returns true if the user is authenticated, false otherwise
     */
    public static UserData authenticate(DAO dao, String username, String password)
    {
        boolean userExists = false;
        String hashedPassword;
        String salt;

        UserData result = null;

        try
        {
            result = dao.getByUsername(username);
            userExists = true;
            hashedPassword = result.getHashedPassword();
            salt = result.getSalt();
        } catch (Exception e)
        {
            hashedPassword = "000000000000000000000000000=";
            salt = "00000000000=";
            result = null;
        }

        byte[] bDigest = base64ToByte(hashedPassword);
        byte[] bSalt = base64ToByte(salt);

        // Compute the new DIGEST
        byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

        if (! Arrays.equals(proposedDigest, bDigest) && userExists)
        {
            return null;
        }
        else
        {
            return result;
        }
    }


    public static UserData createUser(DAO dao, String username, String password, String email) throws InvalidLoginException
    {
        if (ClientUtils.isEmpty(username) || ClientUtils.isEmpty(password) || username.length() > 100 || email.length() > 100)
        {
            throw new IllegalArgumentException();
        }

        String[] hashAndSalt = getHashedPasswordAndSalt(password);
        return dao.createAccount(username, hashAndSalt[0], hashAndSalt[1], email);
    }

    public static void changePassword(DAO dao, String username, String currentPassword, String newPassword) throws InvalidLoginException
    {
        UserData userData = authenticate(dao, username, currentPassword);
        if (userData == null)
        {
            throw new InvalidLoginException("invalid password");
        }

        String[] hashAndSalt = getHashedPasswordAndSalt(newPassword);
        dao.updatePassword(username, hashAndSalt[0], hashAndSalt[1]);
    }

    // useful for password resets
    public static void changePasswordWithoutValidation(DAO dao, String username, String newPassword) throws InvalidLoginException
    {
        String[] hashAndSalt = getHashedPasswordAndSalt(newPassword);
        dao.updatePassword(username, hashAndSalt[0], hashAndSalt[1]);
    }

    private static String[] getHashedPasswordAndSalt(String password)
    {
        SecureRandom random = getSecureRandom();

        // Salt generation 64 bits long
        byte[] bSalt = new byte[8];
        random.nextBytes(bSalt);

        // Digest computation
        byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
        String hashedPassword = byteToBase64(bDigest);
        String salt = byteToBase64(bSalt);

        return new String[] { hashedPassword, salt };
    }

    public static SecureRandom getSecureRandom()
    {
        try
        {
            // Uses a secure Random not a simple Random
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * From a password, a number of iterations and a salt,
     * returns the corresponding digest
     *
     * @param iterationNb int The number of iterations of the algorithm
     * @param password    String The password to encrypt
     * @param salt        byte[] The salt
     * @return byte[] The digested password
     */
    public static byte[] getHash(int iterationNb, String password, byte[] salt)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(salt);

            byte[] input = digest.digest(password.getBytes("UTF-8"));
            for (int i = 0; i < iterationNb; i++)
            {
                digest.reset();
                input = digest.digest(input);
            }
            return input;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] base64ToByte(String data)
    {
        try
        {
            return Base64.decode(data);
        } catch (Base64DecoderException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String byteToBase64(byte[] data)
    {
        try
        {
            return Base64.encode(data);
        } catch (Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
