package studio.dinhduc.doctruyen.ui.rule;


import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Dictionary {
    private int BUCKET_COUNT = 1319; //prime number
    final private Bucket[] mBuckets;

    public Dictionary() {

        mBuckets = new Bucket[BUCKET_COUNT];
        for (int i = 0; i < BUCKET_COUNT; i++) {
            mBuckets[i] = new Bucket();
        }
    }

    private int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % BUCKET_COUNT;
    }

    //call hash() to decide which bucket to put it in, do it.
    public void add(String key) {
        key = key.toLowerCase();
        mBuckets[hash(key)].put(key);
    }

    //call hash() to find what bucket it's in, get it from that bucket. 
    public boolean contains(String input) {
        input = input.toLowerCase();
        return mBuckets[hash(input)].get(input);
    }

    public void build(Context context, String filePath) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(filePath));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                add(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    class Bucket {

        private Node first;

        public boolean get(String in) {         //return key true if key exists
            Node next = first;
            while (next != null) {
                if (next.word.equals(in)) {
                    return true;
                }
                next = next.next;
            }
            return false;
        }

        public void put(String key) {
            for (Node curr = first; curr != null; curr = curr.next) {
                if (key.equals(curr.word)) {
                    return;                     //search hit: return
                }
            }
            first = new Node(key, first); //search miss: add new node
        }

        class Node {

            String word;
            Node next;

            public Node(String key, Node next) {
                this.word = key;
                this.next = next;
            }

        }

    }
}