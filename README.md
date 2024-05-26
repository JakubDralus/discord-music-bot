# Rat Party Mix Discord Music Bot

Discord music bot for my [playlist on Spotify](https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh), that has couple of specific commands just to integrate with this playlist (made possible with help of Spotify API).
It can of course play any other music track using [Lavaplayer](https://github.com/lavalink-devs/lavaplayer) that uses YouTube API for streaming and searching tracks. Branded with the name of the playlist just for fun.

## About the playlist

The playlist was created back when I was in high school. It features party hits mainly from years late 2000s and early 2010s (I love 'white girls' music btw) mixed with some recent popular tracks.
I recommend it as a ready-to-go playlist for parties if someone didn't have one already and I acted like it's some real brand (I still do) and it's kinda fun. 
Then I combined this idea with my programming skills and created this bot as a side project. <br>

A friend of mine also got hooked to this idea and created a bot 'tracker' ([Github](https://github.com/zawislakm/RatPartyMixTracker)) for this playlist. This bot notifies followers whenever the tracklist on Spotify 
is updated and shares a randomly chosen daily song at 12PM (CET) via Twitter everyday. Additionally, it is also possible to fetch daily song directly from [RatPartMix API](http://130.61.63.141:8888/docs).

Another friend aka Zuzanna top G - is making thumbnails every year for the playlist using her Photoshop skills. (they kinda fire🔥)

[![image](https://github.com/JakubDralus/Rat-Party-Mix-discord-music-bot/assets/129612952/2992073c-5616-48bd-bf07-d50f61ede836)](https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh)

# Commands

Exact text from `/help` command:

**Music Commands:**

- `/play` `[track]`: Play a specific music track (paste url or provide a youtube search prompt).
  - arg: `track` (text): The track to be played.
- `/now-playing`: display the current playing track info (this embed does not disappear after track stops playing).
- `/queue`: Display the tracks in the queue.
- `/clear-queue`: Removes all tracks from the queue.
- `/shuffle`: Shuffles all tracks in the queue.
- `/pause`: Stop playing the current track.
- `/resume`: Resume the current track.
- `/repeat`: Sets current track to repeat/no-repeat (toggle).
- `/forward` `[seconds]`: forwards the song by amount of seconds provided.
  - arg: `seconds` (number): seconds to be skipped/forwarded in current playing track.
- `/skip` `[count]`: Skip the current track or a specified number of tracks.
  - arg (optional): `count` (number): The number of tracks to skip.
- `/leave`: Make the bot leave the channel.
- `/play-ratpartymix`: Play the Rat Party Mix 2023™ playlist.
- `/play-daily-song`: Play today's daily song.
- `/play-youtube-banger`: play a song from #youtube-only-bangers channel using a selection menu.
- `/yeahbuddy`: Get pumped up for the gym!

**Other Commands:**

- `/help`: Display command list.
- `/info`: Get information about this music bot and link to the playlist.
- `/twitter`: Get information about the Twitter site.

# Rat Part Mix links

- [Spotify playlist](https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh)
- [Twitter bot](https://twitter.com/RatPartyMix) 'tracker' ([GitHub](https://github.com/zawislakm/RatPartyMixTracker))
- [RatPartMix API](http://130.61.63.141:8888/docs)
- (website coming soon)

# Specification

- Java 19 (it think it can also work on java 17)
- Maven 3.9.6

# Dependencies

You can also find them in pom.xml

- com.github.discord-jda:JDA - v5.0.0-beta.12
- net.dv8tion:JDA - 5.0.0-beta.13
- se.michaelthelin.spotify:spotify-web-api-java - 8.0.0
- dev.arbjerg:lavaplayer - 2.1.0
- org.projectlombok:lombok - 1.18.28
- org.slf4j:slf4j-api - 2.0.5
- org.slf4j:slf4j-log4j12 - 2.0.5
- log4j:log4j - 1.2.17

# Resources:

- https://github.com/discord-jda/JDA
- https://github.com/spotify-web-api-java/spotify-web-api-java
- https://github.com/Walkyst/lavaplayer-fork
- https://github.com/lavalink-devs/lavaplayer (current) <br><br>
- https://github.com/Glaxier0/discord-music-bot
- https://github.com/relaxingleg/Tutorial-Bot
- https://www.youtube.com/watch?v=FvAkzKi9w5s&list=PLMDWhd7MfizXOJXn905x8UqkWtMJ6tl-b
