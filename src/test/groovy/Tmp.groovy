import com.eiryu.nte.bean.Tag
import org.junit.Test

/**
 * Created by eiryu on 2015/05/02.
 */
class Tmp {

//    private static final String INDENT_CHAR = '\t'

    @Test
    void 仮実装をGroovyで書いてみる() {

        def input = new File('src/test/resources/input.txt')

        List<Tag> tags = new ArrayList<Tag>()
        def lastIndentCount = 0

        input.eachLine {
//            println it
            def matcher = (it =~ /(\t*)(.+)/)
            def indentCount = matcher[0][1].count('\t')
//            println indentCount
            def tag = new Tag()
            tag.indentCount = indentCount

            // 最初の空白で分割
            def elements = matcher[0][2].split(/ /, 2)
            tag.name = elements[0]

            if (tags.size() > 0) {
                lastIndentCount = tags.get(tags.size() - 1).indentCount
            }

            // 属性を持たない場合
            if (elements.size() == 1) {
                // インデントが直前より多かったら子要素 または 直前の要素が子要素でそれと同じインデントだったらこちらも子要素
                if (indentCount > lastIndentCount) {
//                if (indentCount > lastIndentCount || (tmpIndentCount == lastIndentCount)) {

                    def lastRootTag = tags.get(tags.size() - 1)
                    if (indentCount - 1 == lastRootTag.indentCount) {
                        lastRootTag.children.add(tag)
                    } else {
                        def t = lastRootTag
                        while (true) {
                            def t2 = t.children.get(t.children.size() - 1)

                            if (indentCount - 1 == t2.indentCount) {
                                t2.children.add(tag)
                                break
                            } else {
                                t = t2.children.get(t2.children.size() - 1)
                            }
                        }
                    }
//                    while (true) {
//                        List<Tag> children = tags.get(tags.size() - 1).children
//                        if (indentCount - 1 == children.get(0).indentCount) {
//                            children.add(tag)
//                            break
//                        }
//                    }

//                    tags.get(tags.size() - 1).getChildren().add(tag)
                } else {
                    tags.add(tag)
                }
                lastIndentCount = indentCount
                return
            }

            def attributes = elements[1].split()

            attributes.each {
//                println it
                def keyAndValue = it.split(/:/, 2)
                tag.attributes.put(keyAndValue[0], keyAndValue[1])
            }

            // インデントが直前より多かったら子要素 または 直前の要素が子要素でそれと同じインデントだったらこちらも子要素
            if (indentCount > lastIndentCount) {
//                if (indentCount > lastIndentCount || (tmpIndentCount == lastIndentCount)) {

                def lastRootTag = tags.get(tags.size() - 1)
                if (indentCount - 1 == lastRootTag.indentCount) {
                    lastRootTag.children.add(tag)
                } else {
                    def t = lastRootTag
                    while (true) {
                        def t2 = t.children.get(t.children.size() - 1)

                        if (indentCount - 1 == t2.indentCount) {
                            t2.children.add(tag)
                            break
                        } else {
                            t = t2.children.get(t2.children.size() - 1)
                        }
                    }
                }
//                    while (true) {
//                        List<Tag> children = tags.get(tags.size() - 1).children
//                        if (indentCount - 1 == children.get(0).indentCount) {
//                            children.add(tag)
//                            break
//                        }
//                    }

//                    tags.get(tags.size() - 1).getChildren().add(tag)
            } else {
                tags.add(tag)
            }

            lastIndentCount = indentCount


        }
        show(tags)
    }

    def void show(List<Tag> tags) {
//        println tags
        tags.each {
            print '\t' * it.indentCount
            showTag(it)

            if (it.children.size() > 0) {
                show(it.children)
                print '\t' * it.indentCount
                println "</${it.name}>"
            }
        }
    }

    def showTag(Tag tag) {
        def a = ''
        tag.attributes.each {
            if ('' == it.key) {
                return;
            }
            a += " ${it.key}=\"${it.value}\""
        }
        print "<${tag.name}${a}>${tag.attributes['']}"
        if (tag.children.size() == 0) {
            print "</${tag.name}>"
        }
        println ''
    }
}
