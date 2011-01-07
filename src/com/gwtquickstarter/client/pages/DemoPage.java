package com.gwtquickstarter.client.pages;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.BasePlace;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class DemoPage extends Composite
{
    interface MyUiBinder extends UiBinder<HTMLPanel, DemoPage>
    {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    HTML lessonLabel;

    public DemoPage()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setLesson(String lessonId)
    {
        lessonLabel.setText(lessonId);
    }

    public static class DemoActivity extends AbstractActivity
    {
        private final DemoPage page;
        private final DemoPlace place;

        public DemoActivity(DemoPage page, DemoPlace place)
        {
            this.page = page;
            this.place = place;
        }

        @Override
        public void start(AcceptsOneWidget containerWidget, EventBus eventBus)
        {
            page.setLesson(place.getLessonId());
            containerWidget.setWidget(page);
        }
    }

    public static class DemoPlace extends BasePlace
    {
        private String lessonId;

        public DemoPlace(String lessonId)
        {
            this.lessonId = lessonId;
        }

        public String getLessonId()
        {
            return lessonId;
        }

    }

    public static class Tokenizer implements PlaceTokenizer<DemoPlace>
    {
        @Override
        public String getToken(DemoPlace place)
        {
            return place.getLessonId();
        }

        @Override
        public DemoPlace getPlace(String token)
        {
            return new DemoPlace(token);
        }
    }

}
