package com.naran.addresmanager1;

import com.naran.addressmanager.TextArticleTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darhandarhad on 2018/12/23.
 */

public class AddressChangeTask {
    private static AddressChangeTask instance;
    private List<AddressChangeListener> addressChangeListeners = new ArrayList<>();
    public static AddressChangeTask getInstance(){
        if(null == instance){
            instance = new AddressChangeTask();
        }
        return instance;
    }
    public void addOnAddressChangeListener(AddressChangeListener addressChangeListener){
        if(!addressChangeListeners.contains(addressChangeListener)){
            addressChangeListeners.add(addressChangeListener);
        }
    }
    public void removeOnAddressChangeListener(AddressChangeListener addressChangeListener){
        if(addressChangeListeners.contains(addressChangeListener)){
            addressChangeListeners.remove(addressChangeListener);
        }
    }
    public void fireMsg(int tag,TextArticleTitle tat){
        for(AddressChangeListener acl : addressChangeListeners){
            acl.onAddressChange(tag,tat);
        }
    }
}
