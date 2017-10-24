package ec.edu.lexus.facebookrecipies.lib.base;

import android.widget.ImageView;

/**
 * Created by Alexis on 08/09/2017.
 */
public interface ImageLoader {
    void load(ImageView imgAvatar, String url);
    void setOnFinishedImageLoadingListener(Object listener);
}
