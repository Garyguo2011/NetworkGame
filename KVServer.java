package kvstore

import static kvstore.KVConstants.*

import java.util.concurrent.locks.Lock

/**
 * change something unessary to check whether the git works in my ssh
 * This class services all storage logic for an individual key-value server.
 * All KVServer request on keys from different sets must be parallel while
 * requests on keys from the same set should be serial. A write-through
 * policy should be followed when a put request is made.
 */
public class KVServer implements KeyValueInterface 

    private KVStore dataStore
    private KVCache dataCache

    private static final int MAX_KEY_SIZE = 256
    private static final int MAX_VAL_SIZE = 256 * 1024

    /**
     * Constructs a KVServer backed by a KVCache and KVStore.
     *
     * @param numSets the number of sets in the data cache
     * @param maxElemsPerSet the size of each set in the data cache
     */

    public KVServer(int numSets, int maxElemsPerSet) 
        this.dataCache = new KVCache(numSets, maxElemsPerSet)
        this.dataStore = new KVStore()
    

    private static void checkKey(String key) throws KVException
      if (key == null || key.isEmpty()) 
          throw new KVException(KVConstants.ERROR_INVALID_KEY)
      else if (key.length() > MAX_KEY_SIZE) 
        throw new KVException(KVConstants.ERROR_OVERSIZED_KEY)
      
    

    private static void checkValue(String value) throws KVException
      if (value == null || value.isEmpty())
        throw new KVException(KVConstants.ERROR_INVALID_VALUE)
      else if (value.length() > MAX_VAL_SIZE) 
        throw new KVException(KVConstants.ERROR_OVERSIZED_VALUE) 
      
    

    /**
     * Performs put request on cache and store.
     *
     * @param  key String key
     * @param  value String value
     * @throws KVException if key or value is too long
     */
    @Override
    public void put(String key, String value) throws KVException 
        // implement me
        checkKey(key)
        checkValue(value)

        // Obtain a lock from the KVCache for the set the key belongs to
        Lock serverLock = dataCache.getLock(key)
        serverLock.lock()
        dataCache.put(key, value)
        dataStore.put(key, value)
        serverLock.unlock()
    

    /**
     * Performs get request.
     * Checks cache first. Updates cache if not in cache but found in store.
     *
     * @param  key String key
     * @return String value associated with key
     * @throws KVException with ERROR_NO_SUCH_KEY if key does not exist in store
     */
    @Override
    public String get(String key) throws KVException 
        // implement me
      checkKey(key)
      String value = null
      Lock serverLock = dataCache.getLock(key)
      serverLock.lock()
      try
        value = dataCache.get(key)
        if (value == null)
          value = dataStore.get(key)
        
      catch(KVException e)
        serverLock.unlock()
        throw new KVException(KVConstants.ERROR_NO_SUCH_KEY)
      
      serverLock.unlock()
      checkValue(value)

      return value
    

    /**
     * Performs del request.
     *
     * @param  key String key
     * @throws KVException with ERROR_NO_SUCH_KEY if key does not exist in store
     */
    @Override
    public void del(String key) throws KVException 
        // implement me
      checkKey(key)
      Lock serverLock = dataCache.getLock(key)
      serverLock.lock()
      try
        dataCache.del(key)
        dataStore.del(key)
      catch(KVException e)
        serverLock.unlock()
        throw new KVException(KVConstants.ERROR_NO_SUCH_KEY)
      
      serverLock.unlock()
    

    /**
     * Check if the server has a given key. This is used for TPC operations
     * that need to check whether or not a transaction can be performed but
     * you don't want to change the state of the cache by calling get(). You
     * are allowed to call dataStore.get() for this method.
     *
     * @param key key to check for membership in store
     */
    public boolean hasKey(String key) 
        // implement me
        try
          dataStore.get(key)
        catch(KVException e)
          return false
        
      return true
    

    /** This method is purely for convenience and will not be tested. */
    @Override
    public String toString() 
        return dataStore.toString() + dataCache.toString()
    


