
package ws.object;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Page complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="Page"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="currentPage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="maxPage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="minPage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="pageRow" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="totalPage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="totalRow" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Page", propOrder = {
    "currentPage",
    "maxPage",
    "minPage",
    "pageRow",
    "totalPage",
    "totalRow"
})
public class Page {

    protected Integer currentPage;
    protected Integer maxPage;
    protected Integer minPage;
    protected Integer pageRow;
    protected Integer totalPage;
    protected Integer totalRow;

    /**
     * ��ȡcurrentPage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * ����currentPage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCurrentPage(Integer value) {
        this.currentPage = value;
    }

    /**
     * ��ȡmaxPage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxPage() {
        return maxPage;
    }

    /**
     * ����maxPage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxPage(Integer value) {
        this.maxPage = value;
    }

    /**
     * ��ȡminPage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinPage() {
        return minPage;
    }

    /**
     * ����minPage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinPage(Integer value) {
        this.minPage = value;
    }

    /**
     * ��ȡpageRow���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageRow() {
        return pageRow;
    }

    /**
     * ����pageRow���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageRow(Integer value) {
        this.pageRow = value;
    }

    /**
     * ��ȡtotalPage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * ����totalPage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalPage(Integer value) {
        this.totalPage = value;
    }

    /**
     * ��ȡtotalRow���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalRow() {
        return totalRow;
    }

    /**
     * ����totalRow���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalRow(Integer value) {
        this.totalRow = value;
    }

}
