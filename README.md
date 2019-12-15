# NetEaseMusic2XSPF

把网易云音乐歌单导出成[xspf](http://www.xspf.org/)格式

# 用法

编译的胖jar包在`out`目录下

```shell script
java -jar netease_music_to_xspf-assembly-0.1.jar <url> <filename>
```

例如
```shell script
java -jar netease_music_to_xspf-assembly-0.1.jar "https://music.163.com/#/playlist?id=2092555694" "./xiyang"
```

产生的xspf可以通过别的服务导入到其他音乐平台

例如soundiiz