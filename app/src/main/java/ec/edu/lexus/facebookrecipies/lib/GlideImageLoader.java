package ec.edu.lexus.facebookrecipies.lib;

import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import ec.edu.lexus.facebookrecipies.lib.base.ImageLoader;


/**
 * Created by Alexis on 26/09/2017.
 */

public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;
    private RequestListener onFinishedLoadingListener;

    public GlideImageLoader(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }


    @Override
    public void load(ImageView imgAvatar, String url) {
        RequestOptions requestOptions = new RequestOptions();
            requestOptions
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


        if (onFinishedLoadingListener!=null){
            glideRequestManager
                    .load(url)
                    .apply(requestOptions)
                    .listener(onFinishedLoadingListener)
                    .into(imgAvatar);
        }else {
            glideRequestManager
                    .load(url)
                    .apply(requestOptions)
                    .into(imgAvatar);
        }

    }

    @Override
    public void setOnFinishedImageLoadingListener(Object listener) {
        if (listener instanceof RequestListener){
            this.onFinishedLoadingListener=(RequestListener)listener;
        }

    }
}
