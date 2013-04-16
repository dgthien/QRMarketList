package com.qrmarketlist.market.core.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.qrmarketlist.market.core.AbstractEntity;

@Entity
@Table(name = "QRMARKET_PRODUCT")
@PrimaryKeyJoinColumn(name = "ID")
public class Product extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private ProductEnum status;
	private Double price;
	private Boolean qrCodePrinted;

	@Column(name = "NAME", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 200, nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 50, nullable = false)
	public ProductEnum getStatus() {
		return status;
	}

	public void setStatus(ProductEnum status) {
		this.status = status;
	}

	@Column(name = "PRICE", nullable = true, columnDefinition="Decimal(10,2)")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "QRCODE_PRINTED", nullable = true)
	public Boolean getQrCodePrinted() {
		return qrCodePrinted;
	}

	public void setQrCodePrinted(Boolean qrCodePrinted) {
		this.qrCodePrinted = qrCodePrinted;
	}
}
