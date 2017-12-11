
package ws.object;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Page complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取currentPage属性的值。
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
     * 设置currentPage属性的值。
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
     * 获取maxPage属性的值。
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
     * 设置maxPage属性的值。
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
     * 获取minPage属性的值。
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
     * 设置minPage属性的值。
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
     * 获取pageRow属性的值。
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
     * 设置pageRow属性的值。
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
     * 获取totalPage属性的值。
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
     * 设置totalPage属性的值。
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
     * 获取totalRow属性的值。
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
     * 设置totalRow属性的值。
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
