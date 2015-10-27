package contatos.treinamento.com.br.contatos.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class Contact implements Parcelable {
    private Long id;
    private String photo;
    private String name;
    private Date birth;
    private String webSite;
    private Float rating;
    private String telephone;
    private String email;
    private Date lastDateModified;




    @Override
    public String toString() {
        return this.name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastDateModified() {
        return lastDateModified;
    }

    public void setLastDateModified(Date lastDateModified) {
        this.lastDateModified = lastDateModified;
    }

    public Contact() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id == null ? -1 : this.id);
        dest.writeString(this.photo);
        dest.writeString(this.name == null ? "" : this.name);
        dest.writeLong(birth != null ? birth.getTime() : -1);
        dest.writeString(this.webSite == null ? "" : this.webSite);
        dest.writeFloat(this.rating == null ? 0 : this.rating);
        dest.writeString(this.telephone == null? "" : this.telephone);
        dest.writeString(this.email == null? "" : this.email);
        dest.writeLong(lastDateModified == null?-1 : lastDateModified.getTime());
    }

    protected Contact(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.photo = in.readString();
        this.name = in.readString();
        long tmpBirth = in.readLong();
        this.birth = tmpBirth == -1 ? null : new Date(tmpBirth);
        this.webSite = in.readString();
        this.rating = in.readFloat();
        this.telephone = in.readString();
        this.email = in.readString();
        long tmpLastDateModified = in.readLong();
        this.lastDateModified = tmpLastDateModified == -1? null : new Date(tmpLastDateModified);
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
