
package ws.object;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>ArrayOfNewOldCard complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfNewOldCard"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NewOldCard" type="{http://object.ws}NewOldCard" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfNewOldCard", propOrder = {
    "newOldCard"
})
public class ArrayOfNewOldCard {

    @XmlElement(name = "NewOldCard", nillable = true)
    protected List<NewOldCard> newOldCard;

    /**
     * Gets the value of the newOldCard property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the newOldCard property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNewOldCard().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NewOldCard }
     * 
     * 
     */
    public List<NewOldCard> getNewOldCard() {
        if (newOldCard == null) {
            newOldCard = new ArrayList<NewOldCard>();
        }
        return this.newOldCard;
    }

}
