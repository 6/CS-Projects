/*
 File:       ClipSound.java
 Author:     Martha Witick, Peter Graham
 Previous Authors: Katherine Smith, Leah Perlmutter, Dale Skrien
 Class:      CS 361, Fall 2010
 Project:    8
 Date:       10 December
*/
package project8;

import javax.sound.sampled.*;
import java.io.*;

/**
 * This class models a sampled sound file.
 * It knows how to play the sound and it knows the sound's duration.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class ClipSound implements Sound
{
    /** the raw audio data read in from the file */
    private byte[] audioData;
    /** the duration of this sound in milliseconds*/
    private int duration;
    /** the type info required in order to play this sound*/
    private DataLine.Info info;

    /**
     * creates a ClipSound from a sound file.
     * @param soundFileName the name of the sound file to be used in
     *                      creating the ClipSound
     */
    public ClipSound(String soundFileName)
    {
        this(new File(soundFileName));
    }

    /**
     * creates a ClipSound from a File.
     * precondition:  the file size is at most 2^31-1 bytes (~ 2GB)
     * The program exits if the file is not a valid sampled sound file.
     * This constructor loads the whole sound file into memory and so
     * may take a while for large files.
     * @param soundFile the File containing the sound to be used in the
     *                  ClipSound
     */
    public ClipSound(File soundFile)
    {
        try {
            // read the whole file into our audio byte array
            // assumes the size of the sound file is not too large
            int length = (int) soundFile.length();
            audioData = new byte[length];

            FileInputStream fileStream = new FileInputStream(soundFile);
            int offset = 0;
            while (true) {
                offset = fileStream.read(audioData, offset, length - offset);
                if (offset == -1) {
                    break;
                }
            }

            //calculate the duration ahead of time because calculating it is
            //time consuming
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(audioData));
            AudioFormat format = audioStream.getFormat();
            double framerate = format.getFrameRate(); //frames/sec
            double duration = audioStream.getFrameLength() / framerate; //secs
            this.duration = (int) (1000 * duration);  //milliseconds

            //initialize info using the audioStream's format
            int bufferSize = ((int) audioStream.getFrameLength() *
                    format.getFrameSize());
            this.info = new DataLine.Info(Clip.class,
                    audioStream.getFormat(),
                    bufferSize);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        } catch (UnsupportedAudioFileException uafe) {
            uafe.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * @return the number of milliseconds the sound plays
     */
    public int getDuration()
    {
        return duration;
    }

    /**
     * plays the sound immediately.
     * To avoid tying up the calling thread,
     *   it creates a separate thread in which it executes.
     * It prints a stack trace if any errors occur.
     */
    public void play()
    {
        play(0);
    }

    /**
     * sleeps for the given delay and then plays the sound.
     * To avoid tying up the calling thread,
     *   it creates a separate thread in which it executes.
     * It prints a stack trace if any errors occur.
     *
     * @param delay the number of milliseconds it delays before playing.
     *   If delay < 0, then the first |delay| milliseconds of the clip are
     *   skipped.  If delay < 0 and |delay| > the duration of the clip, then
     *   nothing is played.
     */
    public void play(final int delay)
    {
        new Thread()        //put it all in a new Thread
        {
            public void run()
            {
                try {
                    //first sleep the given offset
                    Thread.sleep(delay < 0 ? 0 : delay);
                    //now play the clip
                    Clip clip = (Clip) AudioSystem.getLine(info);
                    clip.open(AudioSystem.getAudioInputStream(
                            new ByteArrayInputStream(audioData)));
                    if (delay >= 0)
                        clip.setFramePosition(0);
                    else {
                        double frameRate = clip.getFormat().getFrameRate();
                        int startframe = (int) (-delay * frameRate / 1000.0);
                        clip.setFramePosition(startframe);
                    }
                    clip.start();
			        //clip.drain(); //does same as next loop
                    while (clip.isActive()) { //wait until the clip is done
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                       }
                    clip.stop();
                    clip.close();
                } catch (UnsupportedAudioFileException uafe) {
                    uafe.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (LineUnavailableException lue) {
                    lue.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }.start();  //start the thread running
    }
}