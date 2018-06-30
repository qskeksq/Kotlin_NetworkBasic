package network.kotlin.flow9.net.networkbasic.rss

import android.graphics.drawable.Drawable

class RSSNewsItem {

    var title: String? = null
    var link: String? = null
    var description: String? = null
    var pubDate: String? = null
    var author: String? = null
    var category: String? = null

    /**
     * Get icon
     *
     * @return
     */
    /**
     * Set icon
     *
     * @param icon
     */
    var icon: Drawable? = null

    /**
     * Initialize with icon and data array
     */
    constructor() {}

    /**
     * Initialize with icon and strings
     */
    constructor(title: String?, link: String?, description: String?, pubDate: String?
                , author: String?, category: String?) {
        this.title = title
        this.link = link
        this.description = description
        this.pubDate = pubDate
        this.author = author
        this.category = category
    }

    /**
     * Compare with the input object
     *
     * @param other
     * @return
     */
    operator fun compareTo(other: RSSNewsItem): Int {
        if (title == other.title) {
            return -1
        } else if (link == other.link) {
            return -1
        } else if (description == other.description) {
            return -1
        } else if (pubDate == other.pubDate) {
            return -1
        } else if (author == other.author) {
            return -1
        } else if (category == other.category) {
            return -1
        }

        return 0
    }

}
