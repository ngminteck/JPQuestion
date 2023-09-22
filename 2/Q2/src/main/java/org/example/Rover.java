package org.example;

public class Rover
{
    float x;
    float y;
    int dir;

    Rover(float posX, float posY, int facing)
    {
        x = posX;
        y = posY;
        dir = facing;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public String GetFacingString()
    {
        if(dir == 1)
            return "North";
        if(dir == 2)
            return "East";
        if(dir == 3)
            return "South";
        if(dir == 4)
            return "West";

        return "";
    }

    public void RotateLeft()
    {
        --dir;
        if(dir < 1)
            dir = 4;
    }

    public void RotateRight()
    {
        ++dir;
        if(dir > 4)
            dir = 1;
    }
    public void MoveForward()
    {
        if(dir == 1)
            y = y + 1.0f;
        if(dir == 2)
            x = x + 1.0f;
        if(dir == 3)
            y = y -1.0f;
        if(dir == 4)
            x = x - 1.0f;
    }

    public void MoveBackward()
    {
        if(dir == 1)
            y = y - 1.0f;
        if(dir == 2)
            x = x - 1.0f;
        if(dir == 3)
            y = y  + 1.0f;
        if(dir == 4)
            x = x  + 1.0f;
    }

}
