
package ws.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SvcResp complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SvcResp"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="mchInfo" type="{http://object.ws}ArrayOfMchInfo" minOccurs="0"/&gt;
 *         &lt;element name="newOldCard" type="{http://object.ws}ArrayOfNewOldCard" minOccurs="0"/&gt;
 *         &lt;element name="page" type="{http://object.ws}Page" minOccurs="0"/&gt;
 *         &lt;element name="rc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rcDetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="trans" type="{http://object.ws}ArrayOfTrans" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvcResp", propOrder = {
    "mchInfo",
    "newOldCard",
    "page",
    "rc",
    "rcDetail",
    "trans"
})
public class SvcResp {

    @XmlElementRef(name = "mchInfo", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfMchInfo> mchInfo;
    @XmlElementRef(name = "newOldCard", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfNewOldCard> newOldCard;
    @XmlElementRef(name = "page", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<Page> page;
    @XmlElementRef(name = "rc", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rc;
    @XmlElementRef(name = "rcDetail", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rcDetail;
    @XmlElementRef(name = "trans", namespace = "http://object.ws", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTrans> trans;

    /**
     * ��ȡmchInfo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMchInfo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfMchInfo> getMchInfo() {
        return mchInfo;
    }

    /**
     * ����mchInfo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMchInfo }{@code >}
     *     
     */
    public void setMchInfo(JAXBElement<ArrayOfMchInfo> value) {
        this.mchInfo = value;
    }

    /**
     * ��ȡnewOldCard���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfNewOldCard }{@code >}
     *     
     */
    public JAXBElement<ArrayOfNewOldCard> getNewOldCard() {
        return newOldCard;
    }

    /**
     * ����newOldCard���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfNewOldCard }{@code >}
     *     
     */
    public void setNewOldCard(JAXBElement<ArrayOfNewOldCard> value) {
        this.newOldCard = value;
    }

    /**
     * ��ȡpage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Page }{@code >}
     *     
     */
    public JAXBElement<Page> getPage() {
        return page;
    }

    /**
     * ����page���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Page }{@code >}
     *     
     */
    public void setPage(JAXBElement<Page> value) {
        this.page = value;
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
     * ��ȡtrans���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTrans }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTrans> getTrans() {
        return trans;
    }

    /**
     * ����trans���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTrans }{@code >}
     *     
     */
    public void setTrans(JAXBElement<ArrayOfTrans> value) {
        this.trans = value;
    }

}
