package com.cricump.net;

/**
 * Created by IntelliJ IDEA.
 * User: sam
 * Date: 5/09/11
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientCallback {

    public void onSuccess(Object o);

    public void onFailure(Object o);

}
