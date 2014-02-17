package nju.edu.p14.model;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */

public class Product implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2661890288133180791L;
	private String id;
	private String title;
	private String brand;
	private String model;
	private String sku;
	private Float price;
	private Float rating;
	private Integer reviewCount;
	private String store;
	private String category;
	private Integer rankBySelling;
	private Integer rankByPrice;
	private Integer rankByRating;
	private Integer rankByArrival;
	private String url;

	// Constructors

	/** default constructor */
	public Product() {
	}

	/** minimal constructor */
	public Product(String store, String category, Integer rankBySelling,
			Integer rankByPrice) {
		this.store = store;
		this.category = category;
		this.rankBySelling = rankBySelling;
		this.rankByPrice = rankByPrice;
	}

	/** full constructor */
	public Product(String title, String brand, String model, String sku,
			Float price, Float rating, Integer reviewCount, String store,
			String category, Integer rankBySelling, Integer rankByPrice,
			Integer rankByRating, Integer rankByArrival) {
		this.title = title;
		this.brand = brand;
		this.model = model;
		this.sku = sku;
		this.price = price;
		this.rating = rating;
		this.reviewCount = reviewCount;
		this.store = store;
		this.category = category;
		this.rankBySelling = rankBySelling;
		this.rankByPrice = rankByPrice;
		this.rankByRating = rankByRating;
		this.rankByArrival = rankByArrival;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getRating() {
		return this.rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public Integer getReviewCount() {
		return this.reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getStore() {
		return this.store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getRankBySelling() {
		return this.rankBySelling;
	}

	public void setRankBySelling(Integer rankBySelling) {
		this.rankBySelling = rankBySelling;
	}

	public Integer getRankByPrice() {
		return this.rankByPrice;
	}

	public void setRankByPrice(Integer rankByPrice) {
		this.rankByPrice = rankByPrice;
	}

	public Integer getRankByRating() {
		return this.rankByRating;
	}

	public void setRankByRating(Integer rankByRating) {
		this.rankByRating = rankByRating;
	}

	public Integer getRankByArrival() {
		return this.rankByArrival;
	}

	public void setRankByArrival(Integer rankByArrival) {
		this.rankByArrival = rankByArrival;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}