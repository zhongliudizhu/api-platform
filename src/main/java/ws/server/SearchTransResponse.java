
package ws.server;

import ws.object.SvcResp;

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
 *         &lt;element name="out" type="{http://object.ws}SvcResp"/&gt;
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
@XmlRootElement(name = "searchTransResponse")
public class SearchTransResponse {

    @XmlElement(required = true, nillable = true)
    protected SvcResp out;

    /**
     * ��ȡout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SvcResp }
     *     
     */
    public SvcResp getOut() {
        return out;
    }

    /**
     * ����out���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SvcResp }
     *     
     */
    public void setOut(SvcResp value) {
        this.out = value;
    }

}
