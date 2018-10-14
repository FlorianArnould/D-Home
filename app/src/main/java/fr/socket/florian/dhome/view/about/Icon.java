package fr.socket.florian.dhome.view.about;

import android.support.annotation.DrawableRes;

class Icon {
    @DrawableRes
    private final int drawableRes;
    private final String message;
    private final String link;

    Icon(@DrawableRes int drawableRes, String message, String link) {
        this.drawableRes = drawableRes;
        this.message = message;
        this.link = link;
    }


    public int getDrawableRes() {
        return drawableRes;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }
}
