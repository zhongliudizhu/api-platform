
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Trans complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Trans"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="acqInstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dscntAmt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mchName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rrn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settAmt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="subName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="systrace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tranStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnAmt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnAmt2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Trans", propOrder = {
    "accno",
    "account",
    "acqInstName",
    "dscntAmt",
    "mchName",
    "mid",
    "pan",
    "rrn",
    "score",
    "settAmt",
    "subName",
    "systrace",
    "tid",
    "tranStatus",
    "txnAmt",
    "txnAmt2",
    "txnDate",
    "txnName"
})
public class Trans {

    @XmlElementRef(name = "accno", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> accno;
    @XmlElementRef(name = "account", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> account;
    @XmlElementRef(name = "acqInstName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> acqInstName;
    @XmlElementRef(name = "dscntAmt", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dscntAmt;
    @XmlElementRef(name = "mchName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mchName;
    @XmlElementRef(name = "mid", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mid;
    @XmlElementRef(name = "pan", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pan;
    @XmlElementRef(name = "rrn", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rrn;
    @XmlElementRef(name = "score", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> score;
    @XmlElementRef(name = "settAmt", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> settAmt;
    @XmlElementRef(name = "subName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> subName;
    @XmlElementRef(name = "systrace", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> systrace;
    @XmlElementRef(name = "tid", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tid;
    @XmlElementRef(name = "tranStatus", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tranStatus;
    @XmlElementRef(name = "txnAmt", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnAmt;
    @XmlElementRef(name = "txnAmt2", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnAmt2;
    @XmlElementRef(name = "txnDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnDate;
    @XmlElementRef(name = "txnName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnName;

    /**
     * 获取accno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccno() {
        return accno;
    }

    /**
     * 设置accno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccno(JAXBElement<String> value) {
        this.accno = value;
    }

    /**
     * 获取account属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccount() {
        return account;
    }

    /**
     * 设置account属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccount(JAXBElement<String> value) {
        this.account = value;
    }

    /**
     * 获取acqInstName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAcqInstName() {
        return acqInstName;
    }

    /**
     * 设置acqInstName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAcqInstName(JAXBElement<String> value) {
        this.acqInstName = value;
    }

    /**
     * 获取dscntAmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDscntAmt() {
        return dscntAmt;
    }

    /**
     * 设置dscntAmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDscntAmt(JAXBElement<String> value) {
        this.dscntAmt = value;
    }

    /**
     * 获取mchName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMchName() {
        return mchName;
    }

    /**
     * 设置mchName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMchName(JAXBElement<String> value) {
        this.mchName = value;
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
     * 获取rrn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRrn() {
        return rrn;
    }

    /**
     * 设置rrn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRrn(JAXBElement<String> value) {
        this.rrn = value;
    }

    /**
     * 获取score属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getScore() {
        return score;
    }

    /**
     * 设置score属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setScore(JAXBElement<String> value) {
        this.score = value;
    }

    /**
     * 获取settAmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSettAmt() {
        return settAmt;
    }

    /**
     * 设置settAmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSettAmt(JAXBElement<String> value) {
        this.settAmt = value;
    }

    /**
     * 获取subName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSubName() {
        return subName;
    }

    /**
     * 设置subName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubName(JAXBElement<String> value) {
        this.subName = value;
    }

    /**
     * 获取systrace属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSystrace() {
        return systrace;
    }

    /**
     * 设置systrace属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSystrace(JAXBElement<String> value) {
        this.systrace = value;
    }

    /**
     * 获取tid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTid() {
        return tid;
    }

    /**
     * 设置tid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTid(JAXBElement<String> value) {
        this.tid = value;
    }

    /**
     * 获取tranStatus属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTranStatus() {
        return tranStatus;
    }

    /**
     * 设置tranStatus属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTranStatus(JAXBElement<String> value) {
        this.tranStatus = value;
    }

    /**
     * 获取txnAmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnAmt() {
        return txnAmt;
    }

    /**
     * 设置txnAmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnAmt(JAXBElement<String> value) {
        this.txnAmt = value;
    }

    /**
     * 获取txnAmt2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnAmt2() {
        return txnAmt2;
    }

    /**
     * 设置txnAmt2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnAmt2(JAXBElement<String> value) {
        this.txnAmt2 = value;
    }

    /**
     * 获取txnDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnDate() {
        return txnDate;
    }

    /**
     * 设置txnDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnDate(JAXBElement<String> value) {
        this.txnDate = value;
    }

    /**
     * 获取txnName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnName() {
        return txnName;
    }

    /**
     * 设置txnName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnName(JAXBElement<String> value) {
        this.txnName = value;
    }

}
