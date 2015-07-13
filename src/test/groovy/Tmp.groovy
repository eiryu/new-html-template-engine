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

        // 直前のタグ
        Tag lastTag

        input.eachLine {
            def matcher = (it =~ /(\t*)(.+)/)

            // インデント数取得
            def indentCount = matcher[0][1].count('\t')
            def tag = new Tag()
            tag.indentCount = indentCount

            // 最初の空白でタグ名と属性を分割
            //
            // 例）
            // input type:text name:foo value:bar
            // elements[0]: input
            // elements[1]: type:text name:foo value:bar
            def elements = matcher[0][2].split(/ /, 2)
            tag.name = elements[0]

            // 属性を持たない場合
            if (elements.size() == 1) {
                analyzeTagStructure(tags, tag, lastTag)
                lastTag = tag
                return
            }

            def attributes = elements[1].split()

            attributes.each {
                def keyAndValue = it.split(/:/, 2)
                tag.attributes.put(keyAndValue[0], keyAndValue[1])
            }

            analyzeTagStructure(tags, tag, lastTag)
            lastTag = tag
        }
        show(tags)
    }

    def void analyzeTagStructure(List<Tag> tags, Tag tag, Tag lastTag) {
        if (lastTag == null) {
            tags.add(tag)
        } else {
            int indentCount = tag.getIndentCount()
            int lastIndentCount = lastTag.getIndentCount()

            // 直前のタグとインデントが同じ場合は、同じ親タグを持つ
            if (indentCount == lastIndentCount) {
                tag.setParent(lastTag.getParent())
                lastTag.getParent().getChildren().add(tag)

                // 直前のタグよりインデントが多い場合は、直前のタグの子
            } else if (indentCount > lastIndentCount) {
                tag.setParent(lastTag)
                lastTag.getChildren().add(tag)

                // 直前のタグよりインデントが少ない場合は、直前のタグの親を辿ってインデントが少ない物を探してその子タグとする
            } else {
                def tmpParentTag = lastTag.getParent()
                while (true) {
                    // 親タグを持たなかった場合
                    if (tmpParentTag == null) {
                        tags.add(tag)
                        break
                    } else if (indentCount > tmpParentTag.getIndentCount()) {
                        tag.setParent(tmpParentTag)
                        tmpParentTag.getChildren().add(tag)
                        break
                    }
                    tmpParentTag = tmpParentTag.getParent()
                }
            }
        }
    }

    def void show(List<Tag> tags) {
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
