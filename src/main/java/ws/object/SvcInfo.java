
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SvcInfo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡaccGrp���Ե�ֵ��
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
     * ����accGrp���Ե�ֵ��
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
     * ��ȡaccType���Ե�ֵ��
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
     * ����accType���Ե�ֵ��
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
     * ��ȡaccno���Ե�ֵ��
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
     * ����accno���Ե�ֵ��
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
     * ��ȡacqId���Ե�ֵ��
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
     * ����acqId���Ե�ֵ��
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
     * ��ȡauthNo���Ե�ֵ��
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
     * ����authNo���Ե�ֵ��
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
     * ��ȡbalAmt���Ե�ֵ��
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
     * ����balAmt���Ե�ֵ��
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
     * ��ȡbatchNo���Ե�ֵ��
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
     * ����batchNo���Ե�ֵ��
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
     * ��ȡbirthday���Ե�ֵ��
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
     * ����birthday���Ե�ֵ��
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
     * ��ȡbpan���Ե�ֵ��
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
     * ����bpan���Ե�ֵ��
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
     * ��ȡcardStatus���Ե�ֵ��
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
     * ����cardStatus���Ե�ֵ��
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
     * ��ȡcertNo���Ե�ֵ��
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
     * ����certNo���Ե�ֵ��
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
     * ��ȡcertType���Ե�ֵ��
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
     * ����certType���Ե�ֵ��
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
     * ��ȡcustAddr���Ե�ֵ��
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
     * ����custAddr���Ե�ֵ��
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
     * ��ȡcustName���Ե�ֵ��
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
     * ����custName���Ե�ֵ��
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
     * ��ȡcustNo���Ե�ֵ��
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
     * ����custNo���Ե�ֵ��
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
     * ��ȡcvn2���Ե�ֵ��
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
     * ����cvn2���Ե�ֵ��
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
     * ��ȡemail���Ե�ֵ��
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
     * ����email���Ե�ֵ��
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
     * ��ȡexpDate���Ե�ֵ��
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
     * ����expDate���Ե�ֵ��
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
     * ��ȡmcc���Ե�ֵ��
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
     * ����mcc���Ե�ֵ��
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
     * ��ȡmchName���Ե�ֵ��
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
     * ����mchName���Ե�ֵ��
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
     * ��ȡmemo���Ե�ֵ��
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
     * ����memo���Ե�ֵ��
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
     * ��ȡmid���Ե�ֵ��
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
     * ����mid���Ե�ֵ��
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
     * ��ȡmobile���Ե�ֵ��
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
     * ����mobile���Ե�ֵ��
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
     * ��ȡnewPinData���Ե�ֵ��
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
     * ����newPinData���Ե�ֵ��
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
     * ��ȡnewUserPwd���Ե�ֵ��
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
     * ����newUserPwd���Ե�ֵ��
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
     * ��ȡnoPinAmt���Ե�ֵ��
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
     * ����noPinAmt���Ե�ֵ��
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
     * ��ȡnoPinAmtDay���Ե�ֵ��
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
     * ����noPinAmtDay���Ե�ֵ��
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
     * ��ȡopenDate���Ե�ֵ��
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
     * ����openDate���Ե�ֵ��
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
     * ��ȡorgCode���Ե�ֵ��
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
     * ����orgCode���Ե�ֵ��
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
     * ��ȡpan���Ե�ֵ��
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
     * ����pan���Ե�ֵ��
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
     * ��ȡpanIn���Ե�ֵ��
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
     * ����panIn���Ե�ֵ��
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
     * ��ȡphone���Ե�ֵ��
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
     * ����phone���Ե�ֵ��
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
     * ��ȡpinData���Ե�ֵ��
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
     * ����pinData���Ե�ֵ��
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
     * ��ȡrc���Ե�ֵ��
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
     * ����rc���Ե�ֵ��
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
     * ��ȡrcDetail���Ե�ֵ��
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
     * ����rcDetail���Ե�ֵ��
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
     * ��ȡrelatV���Ե�ֵ��
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
     * ����relatV���Ե�ֵ��
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
     * ��ȡrrn���Ե�ֵ��
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
     * ����rrn���Ե�ֵ��
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
     * ��ȡrsvBal���Ե�ֵ��
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
     * ����rsvBal���Ե�ֵ��
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
     * ��ȡrsvdcFlag���Ե�ֵ��
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
     * ����rsvdcFlag���Ե�ֵ��
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
     * ��ȡsettleDate���Ե�ֵ��
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
     * ����settleDate���Ե�ֵ��
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
     * ��ȡsex���Ե�ֵ��
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
     * ����sex���Ե�ֵ��
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
     * ��ȡsmsTag���Ե�ֵ��
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
     * ����smsTag���Ե�ֵ��
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
     * ��ȡsystrace���Ե�ֵ��
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
     * ����systrace���Ե�ֵ��
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
     * ��ȡteller���Ե�ֵ��
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
     * ����teller���Ե�ֵ��
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
     * ��ȡtid���Ե�ֵ��
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
     * ����tid���Ե�ֵ��
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
     * ��ȡtrAmt���Ե�ֵ��
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
     * ����trAmt���Ե�ֵ��
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
     * ��ȡtrk2���Ե�ֵ��
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
     * ����trk2���Ե�ֵ��
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
     * ��ȡtxnDate���Ե�ֵ��
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
     * ����txnDate���Ե�ֵ��
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
     * ��ȡtxnId���Ե�ֵ��
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
     * ����txnId���Ե�ֵ��
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
     * ��ȡtxnTime���Ե�ֵ��
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
     * ����txnTime���Ե�ֵ��
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
     * ��ȡuserCode���Ե�ֵ��
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
     * ����userCode���Ե�ֵ��
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
     * ��ȡuserPwd���Ե�ֵ��
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
     * ����userPwd���Ե�ֵ��
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
     * ��ȡuserType���Ե�ֵ��
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
     * ����userType���Ե�ֵ��
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
     * ��ȡvoucher���Ե�ֵ��
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
     * ����voucher���Ե�ֵ��
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
     * ��ȡywy���Ե�ֵ��
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
     * ����ywy���Ե�ֵ��
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
