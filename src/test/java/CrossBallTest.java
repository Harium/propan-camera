import com.badlogic.gdx.math.Matrix4;
import com.harium.propan.camera.gesture.CrossBall;
import org.junit.Assert;
import org.junit.Test;

public class CrossBallTest {

    @Test
    public void testConstructor() {
        Matrix4 matrix = new Matrix4(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});

        CrossBall crossBall = new CrossBall(matrix);
        Assert.assertArrayEquals(matrix.val, crossBall.getMatrix().val, 0);
    }

}
