package com.company.pubsub;

import java.util.HashMap;

public class PubSubStruct<K, V> {

    private HashMap<K, PubSubNode<V>> hashMap = new HashMap<K, PubSubNode<V>>();

    public synchronized void remove(K key) {
        //returns null if not existent
        if(!hashMap.containsKey(key))
            return;
            //throw new PubSubException("cannot remove non existent key");
        hashMap.remove(key);
    }

    public synchronized int size(){
        return hashMap.size();
    }

    public synchronized void put(K key, V value) {
        if(hashMap.containsKey(key)){
            hashMap.get(key).setValue(value);
        }
        else{
            hashMap.put(key, new PubSubNode<V>(value));
        }

        //hashMap.putIfAbsent(key, new PubSubNode<V>(value)).setValue(value);
    }

    public boolean containsKey(K key) {
        return hashMap.containsKey(key);
    }


    public void clear() {
        hashMap.clear();
    }

    public synchronized UnsubscribeObject subscribe(K key, ListenToNode<V> listener) {
        if (!hashMap.containsKey(key)) {
            throw new RuntimeException("key does not exist");
        }
        PubSubNode<V> item = this.hashMap.get(key);
        return item.subscribe(listener);
     }//subscribe to object whose key in structure is key and val is val // (key,val) is unique
    //caller of this func has an identifier of what it wants to subscribe to, so they can get the key with hash(identifier) amd identifier is val

    public synchronized PubSubNode<V> getValueByKey (K key){
        return this.hashMap.get(key);
    }
}
