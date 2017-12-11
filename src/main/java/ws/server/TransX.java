
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
 *         &lt;element name="in0" type="{http://object.ws}SvcInfo"/&gt;
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
    "in0"
})
@XmlRootElement(name = "transX")
public class TransX {

    @XmlElement(required = true, nillable = true)
    protected SvcInfo in0;

    /**
     * ��ȡin0���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SvcInfo }
     *     
     */
    public SvcInfo getIn0() {
        return in0;
    }

    /**
     * ����in0���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SvcInfo }
     *     
     */
    public void setIn0(SvcInfo value) {
        this.in0 = value;
    }

}
