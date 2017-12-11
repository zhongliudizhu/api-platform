
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TransReq complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="TransReq"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="currentPage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pageRow" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnDate1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnDate2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransReq", propOrder = {
    "cname",
    "currentPage",
    "mid",
    "pageRow",
    "pan",
    "txnDate1",
    "txnDate2",
    "userType"
})
public class TransReq {

    @XmlElementRef(name = "cname", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cname;
    @XmlElementRef(name = "currentPage", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> currentPage;
    @XmlElementRef(name = "mid", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mid;
    @XmlElementRef(name = "pageRow", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pageRow;
    @XmlElementRef(name = "pan", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pan;
    @XmlElementRef(name = "txnDate1", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnDate1;
    @XmlElementRef(name = "txnDate2", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnDate2;
    @XmlElementRef(name = "userType", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> userType;

    /**
     * 获取cname属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCname() {
        return cname;
    }

    /**
     * 设置cname属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCname(JAXBElement<String> value) {
        this.cname = value;
    }

    /**
     * 获取currentPage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCurrentPage() {
        return currentPage;
    }

    /**
     * 设置currentPage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCurrentPage(JAXBElement<String> value) {
        this.currentPage = value;
    }

    /**
     * 获取mid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMid() {
        return mid;
    }

    /**
     * 设置mid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMid(JAXBElement<String> value) {
        this.mid = value;
    }

    /**
     * 获取pageRow属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPageRow() {
        return pageRow;
    }

    /**
     * 设置pageRow属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPageRow(JAXBElement<String> value) {
        this.pageRow = value;
    }

    /**
     * 获取pan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPan() {
        return pan;
    }

    /**
     * 设置pan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPan(JAXBElement<String> value) {
        this.pan = value;
    }

    /**
     * 获取txnDate1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnDate1() {
        return txnDate1;
    }

    /**
     * 设置txnDate1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnDate1(JAXBElement<String> value) {
        this.txnDate1 = value;
    }

    /**
     * 获取txnDate2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnDate2() {
        return txnDate2;
    }

    /**
     * 设置txnDate2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnDate2(JAXBElement<String> value) {
        this.txnDate2 = value;
    }

    /**
     * 获取userType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUserType() {
        return userType;
    }

    /**
     * 设置userType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUserType(JAXBElement<String> value) {
        this.userType = value;
    }

}
