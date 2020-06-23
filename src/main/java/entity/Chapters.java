package entity;
import java.util.Date;
public class Chapters {
	private int id;
	private int fictionId;
	private String title;
	private String content;
	private String html;
	private Date createDate;
	private int sort;

	@Override
	public String toString() {
		return "Chapters{" +
				"id=" + id +
				", fictionId=" + fictionId +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", html='" + html + '\'' +
				", createDate=" + createDate +
				", sort=" + sort +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFictionId() {
		return fictionId;
	}

	public void setFictionId(int fictionId) {
		this.fictionId = fictionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
