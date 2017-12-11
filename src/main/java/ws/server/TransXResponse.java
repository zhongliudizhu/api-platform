
package ws.server;

import ws.object.SvcInfo;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="out" type="{http://object.ws}SvcInfo"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "transXResponse")
public class TransXResponse {

    @XmlElement(required = true, nillable = true)
    protected SvcInfo out;

    /**
     * ��ȡout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SvcInfo }
     *     
     */
    public SvcInfo getOut() {
        return out;
    }

    /**
     * ����out���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SvcInfo }
     *     
     */
    public void setOut(SvcInfo value) {
        this.out = value;
    }

}
