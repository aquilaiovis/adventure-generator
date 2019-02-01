package ch.kbw.update;

import ch.kbw.render.Sprite;

public class Animation
{
    private Sprite[] segments;
    private int currentSegmentIndex, targetTime;
    private long lastSegmentTime;
    private float speedMultiplier;

    public Animation(Sprite[] segments, int targetSegmentsPerSecond)
    {
        this.segments = segments;

        if (targetSegmentsPerSecond > 0)
        {
            targetTime = 1000000000 / targetSegmentsPerSecond;
        }

        currentSegmentIndex = 0;
        lastSegmentTime = System.nanoTime();
        speedMultiplier = 1;
    }

    public Sprite getCurrentSprite()
    {
        // There is no point in incrementing the segments when there is only one
        if (segments.length > 0)
        {
            long currentTime = System.nanoTime();

            if (currentTime > lastSegmentTime + targetTime / speedMultiplier)
            {
                // Reset currentSegmentIndex if at maximum to loop the animation
                if (currentSegmentIndex + 1 >= segments.length) // the >= instead of == is just in case
                {
                    currentSegmentIndex = 0;
                }
                // Increment currentSegmentIndex normally otherwise
                else
                {
                    currentSegmentIndex++;
                }
                lastSegmentTime = currentTime;
            }
        }
        return segments[currentSegmentIndex];
    }

    public void setSpeedMultiplier(float speedMultiplier)
    {
        this.speedMultiplier = speedMultiplier;
    }
}