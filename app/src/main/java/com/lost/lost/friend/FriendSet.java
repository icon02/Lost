package com.lost.lost.friend;


import java.util.HashMap;



public class FriendSet {
    private HashMap<String, String> friendSet;

    public FriendSet() {
        friendSet = new HashMap<>();
    }

    public synchronized void addFriend(String key, String name) {
        friendSet.put(key, name);
    }

    public synchronized void remove(String key) {
        friendSet.remove(key);
    }

    public synchronized void sync(HashMap<String, String> set) {
        friendSet = set;
    }

}
