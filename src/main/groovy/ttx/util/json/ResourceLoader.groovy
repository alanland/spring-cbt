package ttx.util.json

/**
 * Created by journey on 14-12-11.
 */
class ResourceLoader {
    static String configDir = '/home/journey/day/svn/cbt/src/main/resources/config/'

    static String getJsonStringByFile(String file) {
        new File(configDir, file + '.json').getText('utf-8')
    }
}
