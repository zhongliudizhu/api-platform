
package ws.server;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws.server package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws.server
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchMchInfo }
     * 
     */
    public SearchMchInfo createSearchMchInfo() {
        return new SearchMchInfo();
    }

    /**
     * Create an instance of {@link SearchMchInfoResponse }
     * 
     */
    public SearchMchInfoResponse createSearchMchInfoResponse() {
        return new SearchMchInfoResponse();
    }

    /**
     * Create an instance of {@link SearchTrans }
     * 
     */
    public SearchTrans createSearchTrans() {
        return new SearchTrans();
    }

    /**
     * Create an instance of {@link SearchTransResponse }
     * 
     */
    public SearchTransResponse createSearchTransResponse() {
        return new SearchTransResponse();
    }

    /**
     * Create an instance of {@link TransX }
     * 
     */
    public TransX createTransX() {
        return new TransX();
    }

    /**
     * Create an instance of {@link TransXResponse }
     * 
     */
    public TransXResponse createTransXResponse() {
        return new TransXResponse();
    }

}
