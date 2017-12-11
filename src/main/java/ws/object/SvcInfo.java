
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SvcInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SvcInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accGrp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="acqId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="balAmt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="batchNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="birthday" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bpan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cardStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="custAddr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="custName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="custNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cvn2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="expDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mcc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mchName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="memo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newPinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newUserPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="noPinAmt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="noPinAmtDay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="orgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="panIn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rcDetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="relatV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rrn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rsvBal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rsvdcFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settleDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="smsTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="systrace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="teller" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="trAmt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="trk2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="txnTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="voucher" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ywy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvcInfo", propOrder = {
    "accGrp",
    "accType",
    "accno",
    "acqId",
    "authNo",
    "balAmt",
    "batchNo",
    "birthday",
    "bpan",
    "cardStatus",
    "certNo",
    "certType",
    "custAddr",
    "custName",
    "custNo",
    "cvn2",
    "email",
    "expDate",
    "mcc",
    "mchName",
    "memo",
    "mid",
    "mobile",
    "newPinData",
    "newUserPwd",
    "noPinAmt",
    "noPinAmtDay",
    "openDate",
    "orgCode",
    "pan",
    "panIn",
    "phone",
    "pinData",
    "rc",
    "rcDetail",
    "relatV",
    "rrn",
    "rsvBal",
    "rsvdcFlag",
    "settleDate",
    "sex",
    "smsTag",
    "systrace",
    "teller",
    "tid",
    "trAmt",
    "trk2",
    "txnDate",
    "txnId",
    "txnTime",
    "userCode",
    "userPwd",
    "userType",
    "voucher",
    "ywy"
})
public class SvcInfo {

    @XmlElementRef(name = "accGrp", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> accGrp;
    @XmlElementRef(name = "accType", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> accType;
    @XmlElementRef(name = "accno", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> accno;
    @XmlElementRef(name = "acqId", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> acqId;
    @XmlElementRef(name = "authNo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> authNo;
    @XmlElementRef(name = "balAmt", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> balAmt;
    @XmlElementRef(name = "batchNo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> batchNo;
    @XmlElementRef(name = "birthday", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> birthday;
    @XmlElementRef(name = "bpan", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> bpan;
    @XmlElementRef(name = "cardStatus", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cardStatus;
    @XmlElementRef(name = "certNo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> certNo;
    @XmlElementRef(name = "certType", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> certType;
    @XmlElementRef(name = "custAddr", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> custAddr;
    @XmlElementRef(name = "custName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> custName;
    @XmlElementRef(name = "custNo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> custNo;
    @XmlElementRef(name = "cvn2", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cvn2;
    @XmlElementRef(name = "email", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> email;
    @XmlElementRef(name = "expDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> expDate;
    @XmlElementRef(name = "mcc", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mcc;
    @XmlElementRef(name = "mchName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mchName;
    @XmlElementRef(name = "memo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> memo;
    @XmlElementRef(name = "mid", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mid;
    @XmlElementRef(name = "mobile", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mobile;
    @XmlElementRef(name = "newPinData", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> newPinData;
    @XmlElementRef(name = "newUserPwd", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> newUserPwd;
    @XmlElementRef(name = "noPinAmt", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> noPinAmt;
    @XmlElementRef(name = "noPinAmtDay", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> noPinAmtDay;
    @XmlElementRef(name = "openDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> openDate;
    @XmlElementRef(name = "orgCode", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> orgCode;
    @XmlElementRef(name = "pan", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pan;
    @XmlElementRef(name = "panIn", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> panIn;
    @XmlElementRef(name = "phone", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> phone;
    @XmlElementRef(name = "pinData", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pinData;
    @XmlElementRef(name = "rc", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rc;
    @XmlElementRef(name = "rcDetail", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rcDetail;
    @XmlElementRef(name = "relatV", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> relatV;
    @XmlElementRef(name = "rrn", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rrn;
    @XmlElementRef(name = "rsvBal", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rsvBal;
    @XmlElementRef(name = "rsvdcFlag", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rsvdcFlag;
    @XmlElementRef(name = "settleDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> settleDate;
    @XmlElementRef(name = "sex", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> sex;
    @XmlElementRef(name = "smsTag", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> smsTag;
    @XmlElementRef(name = "systrace", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> systrace;
    @XmlElementRef(name = "teller", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> teller;
    @XmlElementRef(name = "tid", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tid;
    @XmlElementRef(name = "trAmt", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> trAmt;
    @XmlElementRef(name = "trk2", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> trk2;
    @XmlElementRef(name = "txnDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnDate;
    @XmlElementRef(name = "txnId", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnId;
    @XmlElementRef(name = "txnTime", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> txnTime;
    @XmlElementRef(name = "userCode", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> userCode;
    @XmlElementRef(name = "userPwd", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> userPwd;
    @XmlElementRef(name = "userType", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> userType;
    @XmlElementRef(name = "voucher", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> voucher;
    @XmlElementRef(name = "ywy", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> ywy;

    /**
     * 获取accGrp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccGrp() {
        return accGrp;
    }

    /**
     * 设置accGrp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccGrp(JAXBElement<String> value) {
        this.accGrp = value;
    }

    /**
     * 获取accType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccType() {
        return accType;
    }

    /**
     * 设置accType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccType(JAXBElement<String> value) {
        this.accType = value;
    }

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
     * 获取acqId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAcqId() {
        return acqId;
    }

    /**
     * 设置acqId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAcqId(JAXBElement<String> value) {
        this.acqId = value;
    }

    /**
     * 获取authNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAuthNo() {
        return authNo;
    }

    /**
     * 设置authNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAuthNo(JAXBElement<String> value) {
        this.authNo = value;
    }

    /**
     * 获取balAmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBalAmt() {
        return balAmt;
    }

    /**
     * 设置balAmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBalAmt(JAXBElement<String> value) {
        this.balAmt = value;
    }

    /**
     * 获取batchNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBatchNo() {
        return batchNo;
    }

    /**
     * 设置batchNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBatchNo(JAXBElement<String> value) {
        this.batchNo = value;
    }

    /**
     * 获取birthday属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBirthday() {
        return birthday;
    }

    /**
     * 设置birthday属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBirthday(JAXBElement<String> value) {
        this.birthday = value;
    }

    /**
     * 获取bpan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBpan() {
        return bpan;
    }

    /**
     * 设置bpan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBpan(JAXBElement<String> value) {
        this.bpan = value;
    }

    /**
     * 获取cardStatus属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCardStatus() {
        return cardStatus;
    }

    /**
     * 设置cardStatus属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCardStatus(JAXBElement<String> value) {
        this.cardStatus = value;
    }

    /**
     * 获取certNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCertNo() {
        return certNo;
    }

    /**
     * 设置certNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCertNo(JAXBElement<String> value) {
        this.certNo = value;
    }

    /**
     * 获取certType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCertType() {
        return certType;
    }

    /**
     * 设置certType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCertType(JAXBElement<String> value) {
        this.certType = value;
    }

    /**
     * 获取custAddr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustAddr() {
        return custAddr;
    }

    /**
     * 设置custAddr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustAddr(JAXBElement<String> value) {
        this.custAddr = value;
    }

    /**
     * 获取custName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustName() {
        return custName;
    }

    /**
     * 设置custName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustName(JAXBElement<String> value) {
        this.custName = value;
    }

    /**
     * 获取custNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustNo() {
        return custNo;
    }

    /**
     * 设置custNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustNo(JAXBElement<String> value) {
        this.custNo = value;
    }

    /**
     * 获取cvn2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCvn2() {
        return cvn2;
    }

    /**
     * 设置cvn2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCvn2(JAXBElement<String> value) {
        this.cvn2 = value;
    }

    /**
     * 获取email属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmail() {
        return email;
    }

    /**
     * 设置email属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmail(JAXBElement<String> value) {
        this.email = value;
    }

    /**
     * 获取expDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getExpDate() {
        return expDate;
    }

    /**
     * 设置expDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setExpDate(JAXBElement<String> value) {
        this.expDate = value;
    }

    /**
     * 获取mcc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMcc() {
        return mcc;
    }

    /**
     * 设置mcc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMcc(JAXBElement<String> value) {
        this.mcc = value;
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
     * 获取memo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMemo() {
        return memo;
    }

    /**
     * 设置memo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMemo(JAXBElement<String> value) {
        this.memo = value;
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
     * 获取mobile属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMobile() {
        return mobile;
    }

    /**
     * 设置mobile属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMobile(JAXBElement<String> value) {
        this.mobile = value;
    }

    /**
     * 获取newPinData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNewPinData() {
        return newPinData;
    }

    /**
     * 设置newPinData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNewPinData(JAXBElement<String> value) {
        this.newPinData = value;
    }

    /**
     * 获取newUserPwd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNewUserPwd() {
        return newUserPwd;
    }

    /**
     * 设置newUserPwd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNewUserPwd(JAXBElement<String> value) {
        this.newUserPwd = value;
    }

    /**
     * 获取noPinAmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNoPinAmt() {
        return noPinAmt;
    }

    /**
     * 设置noPinAmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNoPinAmt(JAXBElement<String> value) {
        this.noPinAmt = value;
    }

    /**
     * 获取noPinAmtDay属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNoPinAmtDay() {
        return noPinAmtDay;
    }

    /**
     * 设置noPinAmtDay属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNoPinAmtDay(JAXBElement<String> value) {
        this.noPinAmtDay = value;
    }

    /**
     * 获取openDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOpenDate() {
        return openDate;
    }

    /**
     * 设置openDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOpenDate(JAXBElement<String> value) {
        this.openDate = value;
    }

    /**
     * 获取orgCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrgCode() {
        return orgCode;
    }

    /**
     * 设置orgCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrgCode(JAXBElement<String> value) {
        this.orgCode = value;
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
     * 获取panIn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPanIn() {
        return panIn;
    }

    /**
     * 设置panIn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPanIn(JAXBElement<String> value) {
        this.panIn = value;
    }

    /**
     * 获取phone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPhone() {
        return phone;
    }

    /**
     * 设置phone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPhone(JAXBElement<String> value) {
        this.phone = value;
    }

    /**
     * 获取pinData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPinData() {
        return pinData;
    }

    /**
     * 设置pinData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPinData(JAXBElement<String> value) {
        this.pinData = value;
    }

    /**
     * 获取rc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRc() {
        return rc;
    }

    /**
     * 设置rc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRc(JAXBElement<String> value) {
        this.rc = value;
    }

    /**
     * 获取rcDetail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRcDetail() {
        return rcDetail;
    }

    /**
     * 设置rcDetail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRcDetail(JAXBElement<String> value) {
        this.rcDetail = value;
    }

    /**
     * 获取relatV属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRelatV() {
        return relatV;
    }

    /**
     * 设置relatV属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRelatV(JAXBElement<String> value) {
        this.relatV = value;
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
     * 获取rsvBal属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRsvBal() {
        return rsvBal;
    }

    /**
     * 设置rsvBal属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRsvBal(JAXBElement<String> value) {
        this.rsvBal = value;
    }

    /**
     * 获取rsvdcFlag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRsvdcFlag() {
        return rsvdcFlag;
    }

    /**
     * 设置rsvdcFlag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRsvdcFlag(JAXBElement<String> value) {
        this.rsvdcFlag = value;
    }

    /**
     * 获取settleDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSettleDate() {
        return settleDate;
    }

    /**
     * 设置settleDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSettleDate(JAXBElement<String> value) {
        this.settleDate = value;
    }

    /**
     * 获取sex属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSex() {
        return sex;
    }

    /**
     * 设置sex属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSex(JAXBElement<String> value) {
        this.sex = value;
    }

    /**
     * 获取smsTag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSmsTag() {
        return smsTag;
    }

    /**
     * 设置smsTag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSmsTag(JAXBElement<String> value) {
        this.smsTag = value;
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
     * 获取teller属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTeller() {
        return teller;
    }

    /**
     * 设置teller属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTeller(JAXBElement<String> value) {
        this.teller = value;
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
     * 获取trAmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTrAmt() {
        return trAmt;
    }

    /**
     * 设置trAmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTrAmt(JAXBElement<String> value) {
        this.trAmt = value;
    }

    /**
     * 获取trk2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTrk2() {
        return trk2;
    }

    /**
     * 设置trk2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTrk2(JAXBElement<String> value) {
        this.trk2 = value;
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
     * 获取txnId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnId() {
        return txnId;
    }

    /**
     * 设置txnId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnId(JAXBElement<String> value) {
        this.txnId = value;
    }

    /**
     * 获取txnTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTxnTime() {
        return txnTime;
    }

    /**
     * 设置txnTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTxnTime(JAXBElement<String> value) {
        this.txnTime = value;
    }

    /**
     * 获取userCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUserCode() {
        return userCode;
    }

    /**
     * 设置userCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUserCode(JAXBElement<String> value) {
        this.userCode = value;
    }

    /**
     * 获取userPwd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUserPwd() {
        return userPwd;
    }

    /**
     * 设置userPwd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUserPwd(JAXBElement<String> value) {
        this.userPwd = value;
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

    /**
     * 获取voucher属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVoucher() {
        return voucher;
    }

    /**
     * 设置voucher属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVoucher(JAXBElement<String> value) {
        this.voucher = value;
    }

    /**
     * 获取ywy属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getYwy() {
        return ywy;
    }

    /**
     * 设置ywy属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setYwy(JAXBElement<String> value) {
        this.ywy = value;
    }

}
