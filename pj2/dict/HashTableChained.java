/* HashTableChained.java */

package dict;

import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  public final static int DEFAULTSIZE = 101;

  private List[] hashTable;
  private int numOfEntries;
  private int numOfCollisions;



  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
    this.hashTable = new DList[sizeEstimate];
    this.numOfEntries = 0;
    this.numOfCollisions = 0;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
    this(DEFAULTSIZE);
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
    int position = ((701 * code + 317) % 16908799) % hashTable.length;
    if (position < 0){
      position += hashTable.length;
    }
    return position;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return numOfEntries;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    return numOfEntries == 0;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
    int position = compFunction(key.hashCode());
    Entry insertEntry = new Entry();
    insertEntry.key = key;
    insertEntry.value = value;

    if (hashTable[position] == null){
      hashTable[position] = new DList();
    }
    hashTable[position].insertBack(insertEntry);
    return insertEntry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    // Replace the following line with your solution.
    int position = compFunction(key.hashCode());
    if (hashTable[position] == null){
      return null;
    }else{
      try{
        ListNode walker = hashTable[position].front();
        if(walker.isValidNode() && ((Entry)walker.item()).key().equals(key)){
          return (Entry) walker.item();
        }
        while(walker.next().isValidNode()){
          if(((Entry)walker.next().item()).key().equals(key)){
            return (Entry)walker.next().item();
          }
          walker = walker.next();
        }
      }catch(InvalidNodeException e){
        System.out.println(e);
      }
      return null;
    }
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    // Replace the following line with your solution.
    int position = compFunction(key.hashCode());
    if (hashTable[position] == null){
      return null;
    }else{
      try{
        ListNode walker = hashTable[position].front();
        if(walker.isValidNode() && ((Entry)walker.item()).key().equals(key)){
          Entry retEntry = (Entry) walker.item();
          walker.remove();
          if (hashTable[position].isEmpty()){
            hashTable[position] = null;
          }
          return retEntry;
        }
        while(walker.next().isValidNode()){
          if(((Entry)walker.next().item()).key().equals(key)){
            Entry retEntry = (Entry)walker.next().item();
            walker.next().remove();
            return retEntry;
          }
          walker = walker.next();
        }
      }catch(InvalidNodeException e){
        System.out.println(e);
      }
      return null;
    }
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    for (int i = 0; i < hashTable.length; i++) {
      hashTable[i] = null;
    }
  }


  /* Addtional function for Collision Analysis */
  public void collisionsRate(){
    int collisions = 0;
    for (int i = 0; i < hashTable.length; i++) {
      if (hashTable[i] != null){
        if(hashTable[i].length() > 1){
          collisions += hashTable[i].length() - 1;
        }
      }
    }
    float colRate = (float) collisions / (float) hashTable.length;
    System.out.println("collisions: " + collisions);
    System.out.println("hashTable: " + hashTable.length);
    System.out.println("collisionsRate: " + colRate);
    return;
  }

  /* Addtional function for print hashtable to string */

  /* toString version 1: */
  /*    
  public String toString(){
    String outString = "";
    for (int i = 0; i < hashTable.length; i++) {
      outString += "Bucket[" + i + "]: "; 
      if (hashTable[i] == null){
        outString += "NULL\n";
      }else{
        try{
          ListNode walker = hashTable[i].front();
          if(walker.isValidNode()){
            outString += "ListSize[" + hashTable[i].length() + "]: ";
            outString += (Integer)((Entry)walker.item()).key() + ": " + (Integer)((Entry)walker.item()).value() + " -> ";
          }
          while(walker.next().isValidNode()){
            outString += (Integer)((Entry)walker.next().item()).key() + ": " + (Integer)((Entry)walker.next().item()).value() + " -> ";
            walker = walker.next();
          }
        }catch(InvalidNodeException e){
          System.out.println(e);
        }
        outString += "\n";
      }
    }
    return outString;
  }
  */

  public String toString(){
    String outString = "";
    for (int i = 0; i < hashTable.length; i++) {
      outString += "Bucket[" + i + "]: "; 
      if (hashTable[i] == null){
        outString += "NULL\n";
      }else{
        try{
          ListNode walker = hashTable[i].front();
          if(walker.isValidNode()){
            outString += "ListSize[" + hashTable[i].length() + "]: ";
            outString += "Bval: " + (Integer)((Entry)walker.item()).value() + " -> ";
          }
          while(walker.next().isValidNode()){
            outString += "Bval: " + (Integer)((Entry)walker.next().item()).value() + " -> ";
            walker = walker.next();
          }
        }catch(InvalidNodeException e){
          System.out.println(e);
        }
        outString += "\n";
      }
    }
    return outString;
  }


}
