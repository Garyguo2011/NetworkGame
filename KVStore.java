package kvstore

import static kvstore.KVConstants.*

import java.util.concurrent.ConcurrentHashMap


// import for coverting KVStore into XML
//=================================================


import java.util.Enumeration
import java.io.StringWriter
import java.io.File
import java.io.FileWriter


import java.io.IOException



import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
 
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.w3c.dom.Text

//=================================================


/**
 * This is a basic key-value store. Ideally this would go to disk, or some other
 * backing store.
 */
public class KVStore implements KeyValueInterface 

    private ConcurrentHashMap<String, String> store

    /**
     * Construct a new KVStore.
     */
    public KVStore() 
        resetStore()
    

    private void resetStore() 
        this.store = new ConcurrentHashMap<String, String>()
    

    /**
     * Insert key, value pair into the store.
     *
     * @param  key String key
     * @param  value String value
     */
    @Override
    public void put(String key, String value) 
        store.put(key, value)
    

    /**
     * Retrieve the value corresponding to the provided key
     * @param  key String key
     * @throws KVException with ERROR_NO_SUCH_KEY if key does not exist in store
     */
    @Override
    public String get(String key) throws KVException 
        String retVal = this.store.get(key)
        if (retVal == null) 
            KVMessage msg = new KVMessage(KVConstants.RESP, ERROR_NO_SUCH_KEY)
            throw new KVException(msg)
        
        return retVal
    

    /**
     * Delete the value corresponding to the provided key.
     *
     * @param  key String key
     * @throws KVException with ERROR_NO_SUCH_KEY if key does not exist in store
     */
    @Override
    public void del(String key) throws KVException 
        if(key != null) 
            if (!this.store.containsKey(key)) 
                KVMessage msg = new KVMessage(KVConstants.RESP, ERROR_NO_SUCH_KEY)
                throw new KVException(msg)
            
            this.store.remove(key)
        
    

    /**
     * Serialize this store to XML. See the spec for specific output format.
     * This method is best effort. Any exceptions that appear can be dropped.
     */
    public String toXML() 
      // implement me

      String output = ""
      try

        // Cite: http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance()
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder()

        Document doc = docBuilder.newDocument()
        Element rootElement = doc.createElement("KVStore")
        doc.appendChild(rootElement)

        Enumeration<String> keysWalker = this.store.keys()
        while (keysWalker.hasMoreElements())
          String key = keysWalker.nextElement()
          String value = this.store.get(key)

          // <KVPair>
          Element pairElement = doc.createElement("KVPair")

          // <Key>key1</Key>
          Element keyElement = doc.createElement("Key")
          keyElement.appendChild(doc.createTextNode(key))
          pairElement.appendChild(keyElement)

          // <Value>value1</Value>
          Element valueElement = doc.createElement("Value")
          valueElement.appendChild(doc.createTextNode(value))
          pairElement.appendChild(valueElement)

          // </KVPair>
          // add into <KVStore>
          rootElement.appendChild(pairElement)
        

        // Cite: http://stackoverflow.com/questions/5456680/xml-document-to-string
        // Cover XML doc to String
        TransformerFactory transFactory = TransformerFactory.newInstance()
        Transformer transformer = transFactory.newTransformer()
        StringWriter writer = new StringWriter()
        transformer.transform(new DOMSource(doc), new StreamResult(writer))
        output = writer.getBuffer().toString()

      catch(Exception e)
        // Any exceptions that appear can be dropped
        return ""
      
      return output        
    

    @Override
    public String toString() 
      return this.toXML()
    

    /**
     * Serialize to XML and write to a file.
     * This method is best effort. Any exceptions that      *
     * @param fileName the file to write the serialized store
     */
    public void dumpToFile(String fileName) 
      // implement me
      try
        File file = new File(fileName)
        FileWriter fileWriter = new FileWriter(file)
        fileWriter.write(this.toXML())
        fileWriter.close()

      catch(Exception e)
        fileWriter.close()
        return
      
      return
    

    /**
     * Replaces the contents of the store with the contents of a file
     * written by dumpToFile the previous contents of the store are lost.
     * The store is cleared even if the file doesn't exist.
     * This method is best effort. Any exceptions that arise can be dropped.
     *
     * @param fileName the file containing the serialized store data
     */
    public void restoreFromFile(String fileName) 
      // implement me
      try
        File file = new File(fileName)
        if(!file.exists() || !file.canRead())
          throw new Exception("File doesn't exist or unreadable")
        

        resetStore()
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance()
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder()
        Document doc = docBuilder.parse(file)
        NodeList pairs = doc.getElementsByTagName("KVStore")

        for (int i = 0 i < pairs.getlength() i++) 
          if(pair.item(i).getLocalName() != "KVPair")
            throw new KVException("The child of KVStore is not KVPair")
          

          NodeList keyValue = pairs.item(i).getChildNodes()
          if (keyValue.getLength() != 2)
            throw new KVException("KV pair doesn't contain two children")
          

          Element keyElement = (Element) keyValue.item(0)
          if (keyElement.getTagName() != "Key") 
            throw new KVException("The first child in KV pair is not a Key")
          

          Element valueElement = (Element) keyValue.item(1)
          if(valueElement.getTagName() != "Value")
            throw new KVException("The second child in KV pair is not a Value")
          

          String key = keyElement.getFirstChild().getNodeValue()
          String value = valueElement.getFirstChild().getNodeValue()

          if(key != null && value != null)
            this.store.put(key, value)  
          
        
      catch(Exception e)
        resetStore()
        return
      
    

