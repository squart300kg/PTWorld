import android.graphics.Bitmap;

public class ProfileDTO {
    String nickname;
    String email;
    Bitmap thumbnail;

    public ProfileDTO(String nickname, String email, Bitmap thumbnail) {
        this.nickname = nickname;
        this.email = email;
        this.thumbnail = thumbnail;
    }
}
