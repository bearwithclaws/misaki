(ns misaki.test.html
  (:use misaki.test.common
        misaki.html.core
        [misaki.core :only [make-site-data]]
        [misaki.config :only [*tag-layout* *site*]]
        [hiccup.core :only [html]])
  (:use [clojure.test])
  (:require [clojure.java.io :as io]))

;; ul
(deftest ul-test
  (are [x y] (= x (html y))
    "<ul><li><span>1</span></li><li><span>2</span></li></ul>"
    (ul [1 2])
    "<ul><li><span>2</span></li><li><span>3</span></li></ul>"
    (ul inc [1 2])
    "<ul><li><span>1</span></li><li><span><ul><li><span>2</span></li></ul></span></li></ul>"
    (ul [1 (ul [2])])
    "<ul><li><span>a</span></li><li><span><code class=\"prettyprint\">b</code></span></li></ul>"
    (ul ["a" "`b`"])))

(deftest img-test
  (is (= "<img alt=\"\" src=\"a.png\" />" (html (img "a.png"))))
  (is (= "<img alt=\"neko\" src=\"a.png\" />" (html (img "neko" "a.png")))))

(deftest link-test
  (are [x y] (= x (html y))
    "<a href=\"a.html\">a.html</a>" (link "a.html")
    "<a href=\"a.html\">link</a>" (link "link" "a.html")
    "<a href=\"a.html\"><code class=\"prettyprint\">link</code></a>" (link "`link`" "a.html")))


(deftest dl-test
  (are [x y] (= x (html y))
    "<dl><dt>a</dt><dd>1</dd></dl>" (dl {:a 1})
    "<dl><dt>a</dt><dd>1</dd></dl>" (dl [:a 1])
    "<dl><dt>a</dt><dd>1</dd><dt>b</dt><dd>2</dd></dl>" (dl [:a 1 :b 2])

    "<dl><dt>a/b</dt><dd>1</dd></dl>" (dl {:a/b 1})

    "<dl><dt><code class=\"prettyprint\">a</code></dt><dd>1</dd></dl>" (dl ["`a`" 1])
    "<dl><dt>a</dt><dd><code class=\"prettyprint\">1</code></dd></dl>" (dl {:a "`1`"})))

(deftest table-test
  (are [x y] (= x (html y))
    "<table><tbody><tr><td>a</td><td>b</td></tr></tbody></table>"
    (table '[[a b]])
    "<table><tbody><tr><td>a</td><td>b</td></tr><tr><td>c</td><td>d</td></tr></tbody></table>"
    (table '[[a b] [c d]])

    "<table><thead><tr><th>1</th><th>2</th></tr></thead><tbody><tr><td>a</td><td>b</td></tr></tbody></table>"
    (table [1 2] '[[a b]])

    "<table><tbody><tr><td><code class=\"prettyprint\">a</code></td><td>b</td></tr></tbody></table>"
    (table [["`a`" "b"]])))

(deftest code-test
  (are [x y] (= x y)
    "<code class=\"prettyprint\">(+ 1 2)</code>" (html (code (+ 1 2)))
    "<code class=\"prettyprint\">(+ 1 2)</code>" (html (code "(+ 1 2)"))))

(deftest* paragraph-test
  (binding [*site* (make-site-data (io/file *tag-layout*))]
    (are [x y] (= x (html y))
      "<p class=\"paragraph\">hello</p>" (p "hello")
      "<p class=\"paragraph\">helloworld</p>" (p "hello" "world")
      "<p class=\"paragraph\">he<code class=\"prettyprint\">ll</code>o</p>" (p "he`ll`o")
      "<p class=\"paragraph\">he<strong>ll</strong>o</p>" (p "he**ll**o")
      "<p class=\"paragraph\">he<em>ll</em>o</p>" (p "he*ll*o")
      "<p class=\"paragraph\"><strong>he</strong>l<em>lo</em></p>" (p "**he**l*lo*")
      "<p class=\"paragraph\"><a href=\"a.html\">a</a></p>" (p "[a](a.html)")
      "<p class=\"paragraph\"><a href=\"/2011-01/foo.html\">a</a></p>" (p "[a](title:foo)")
      "<p class=\"paragraph\"><a href=\"/2011-01/foo.html\">a</a></p>" (p "[a](title: foo)"))))

(deftest css-test
  (are [x y] (= x y)
    "<link href=\"a.css\" rel=\"stylesheet\" type=\"text/css\" />" (html (first (css "a.css")))
    "<link href=\"b.css\" rel=\"stylesheet\" type=\"text/css\" />" (html (second (css "a.css" "b.css")))
    "<link href=\"a.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" />" (html (first (css {:media "screen"} "a.css")))
    "<link href=\"b.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\" />" (html (second (css {:media "screen"} "a.css" "b.css")))))

;(deftest embed-test
;  (are [x y] (= x y)
;    "hello "        (embed "hello ??")
;    "hello "        (embed "hello ??" nil)
;    "hello world"   (embed "hello ??" "world")
;    "hello 1"       (embed "hello ??" 1)
;    "hello 1"       (embed "hello ??" 1 2)
;    "hello 1 world" (embed "hello ?? ??" 1 "world")
;    "hello aa bb"   (embed "hello ??" '("aa" "bb"))))
