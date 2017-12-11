
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Trans complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡaccount���Ե�ֵ��
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
     * ����account���Ե�ֵ��
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
     * ��ȡacqInstName���Ե�ֵ��
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
     * ����acqInstName���Ե�ֵ��
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
     * ��ȡdscntAmt���Ե�ֵ��
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
     * ����dscntAmt���Ե�ֵ��
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
     * ��ȡscore���Ե�ֵ��
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
     * ����score���Ե�ֵ��
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
     * ��ȡsettAmt���Ե�ֵ��
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
     * ����settAmt���Ե�ֵ��
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
     * ��ȡsubName���Ե�ֵ��
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
     * ����subName���Ե�ֵ��
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
     * ��ȡtranStatus���Ե�ֵ��
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
     * ����tranStatus���Ե�ֵ��
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
     * ��ȡtxnAmt���Ե�ֵ��
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
     * ����txnAmt���Ե�ֵ��
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
     * ��ȡtxnAmt2���Ե�ֵ��
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
     * ����txnAmt2���Ե�ֵ��
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
     * ��ȡtxnName���Ե�ֵ��
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
     * ����txnName���Ե�ֵ��
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
