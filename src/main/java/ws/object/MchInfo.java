
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MchInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MchInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="areaCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="brCname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="busType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="buzTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cupMan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="entId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="linkMan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mcc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mchAddr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mchGrp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="memo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="scapeFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="scapeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settleAccno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settleBank" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="signDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MchInfo", propOrder = {
    "accName",
    "areaCode",
    "brCname",
    "busType",
    "buzTime",
    "cname",
    "cupMan",
    "email",
    "ename",
    "entId",
    "fax",
    "linkMan",
    "mcc",
    "mchAddr",
    "mchGrp",
    "memo",
    "mid",
    "openDate",
    "phone",
    "scapeFlag",
    "scapeId",
    "settleAccno",
    "settleBank",
    "signDate",
    "status"
})
public class MchInfo {

    @XmlElementRef(name = "accName", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> accName;
    @XmlElementRef(name = "areaCode", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> areaCode;
    @XmlElementRef(name = "brCname", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> brCname;
    @XmlElementRef(name = "busType", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> busType;
    @XmlElementRef(name = "buzTime", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> buzTime;
    @XmlElementRef(name = "cname", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cname;
    @XmlElementRef(name = "cupMan", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cupMan;
    @XmlElementRef(name = "email", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> email;
    @XmlElementRef(name = "ename", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> ename;
    @XmlElementRef(name = "entId", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> entId;
    @XmlElementRef(name = "fax", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> fax;
    @XmlElementRef(name = "linkMan", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> linkMan;
    @XmlElementRef(name = "mcc", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mcc;
    @XmlElementRef(name = "mchAddr", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mchAddr;
    @XmlElementRef(name = "mchGrp", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mchGrp;
    @XmlElementRef(name = "memo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> memo;
    @XmlElementRef(name = "mid", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mid;
    @XmlElementRef(name = "openDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> openDate;
    @XmlElementRef(name = "phone", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> phone;
    @XmlElementRef(name = "scapeFlag", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> scapeFlag;
    @XmlElementRef(name = "scapeId", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> scapeId;
    @XmlElementRef(name = "settleAccno", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> settleAccno;
    @XmlElementRef(name = "settleBank", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> settleBank;
    @XmlElementRef(name = "signDate", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> signDate;
    @XmlElementRef(name = "status", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> status;

    /**
     * 获取accName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAccName() {
        return accName;
    }

    /**
     * 设置accName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAccName(JAXBElement<String> value) {
        this.accName = value;
    }

    /**
     * 获取areaCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAreaCode() {
        return areaCode;
    }

    /**
     * 设置areaCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAreaCode(JAXBElement<String> value) {
        this.areaCode = value;
    }

    /**
     * 获取brCname属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBrCname() {
        return brCname;
    }

    /**
     * 设置brCname属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBrCname(JAXBElement<String> value) {
        this.brCname = value;
    }

    /**
     * 获取busType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBusType() {
        return busType;
    }

    /**
     * 设置busType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBusType(JAXBElement<String> value) {
        this.busType = value;
    }

    /**
     * 获取buzTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBuzTime() {
        return buzTime;
    }

    /**
     * 设置buzTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBuzTime(JAXBElement<String> value) {
        this.buzTime = value;
    }

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
     * 获取cupMan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCupMan() {
        return cupMan;
    }

    /**
     * 设置cupMan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCupMan(JAXBElement<String> value) {
        this.cupMan = value;
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
     * 获取ename属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEname() {
        return ename;
    }

    /**
     * 设置ename属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEname(JAXBElement<String> value) {
        this.ename = value;
    }

    /**
     * 获取entId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEntId() {
        return entId;
    }

    /**
     * 设置entId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEntId(JAXBElement<String> value) {
        this.entId = value;
    }

    /**
     * 获取fax属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFax() {
        return fax;
    }

    /**
     * 设置fax属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFax(JAXBElement<String> value) {
        this.fax = value;
    }

    /**
     * 获取linkMan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLinkMan() {
        return linkMan;
    }

    /**
     * 设置linkMan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLinkMan(JAXBElement<String> value) {
        this.linkMan = value;
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
     * 获取mchAddr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMchAddr() {
        return mchAddr;
    }

    /**
     * 设置mchAddr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMchAddr(JAXBElement<String> value) {
        this.mchAddr = value;
    }

    /**
     * 获取mchGrp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMchGrp() {
        return mchGrp;
    }

    /**
     * 设置mchGrp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMchGrp(JAXBElement<String> value) {
        this.mchGrp = value;
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
     * 获取scapeFlag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getScapeFlag() {
        return scapeFlag;
    }

    /**
     * 设置scapeFlag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setScapeFlag(JAXBElement<String> value) {
        this.scapeFlag = value;
    }

    /**
     * 获取scapeId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getScapeId() {
        return scapeId;
    }

    /**
     * 设置scapeId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setScapeId(JAXBElement<String> value) {
        this.scapeId = value;
    }

    /**
     * 获取settleAccno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSettleAccno() {
        return settleAccno;
    }

    /**
     * 设置settleAccno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSettleAccno(JAXBElement<String> value) {
        this.settleAccno = value;
    }

    /**
     * 获取settleBank属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSettleBank() {
        return settleBank;
    }

    /**
     * 设置settleBank属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSettleBank(JAXBElement<String> value) {
        this.settleBank = value;
    }

    /**
     * 获取signDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSignDate() {
        return signDate;
    }

    /**
     * 设置signDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSignDate(JAXBElement<String> value) {
        this.signDate = value;
    }

    /**
     * 获取status属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatus(JAXBElement<String> value) {
        this.status = value;
    }

}
