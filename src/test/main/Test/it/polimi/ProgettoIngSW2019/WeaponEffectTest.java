package it.polimi.ProgettoIngSW2019;

import it.polimi.ProgettoIngSW2019.model.WeaponEffect;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class WeaponEffectTest {
    ByteArrayOutputStream outContent;

    @Before
    public void setup(){
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void ShouldCreateEffectWithoutException(){
        WeaponEffect eff = new WeaponEffect("D:\\\\File_JSON\\HammerEff.json");

        assertNotEquals("Error Occurred: WeaponEff file has not found",outContent.toString());
    }

    @Test
    public void ShouldThrowExceptionAndHandled(){
        WeaponEffect eff = new WeaponEffect("");

        assertEquals("Error Occurred: WeaponEff file has not found",outContent.toString());
    }
}
