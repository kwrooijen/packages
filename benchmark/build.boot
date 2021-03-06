(set-env!
 :resource-paths #{"resources"}
 :dependencies '[[cljsjs/boot-cljsjs "0.5.2"  :scope "test"]
                 [cljsjs/lodash      "4.11.2-0"]
                 [cljsjs/platform    "1.3.1-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "2.1.4")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom {:project     'cljsjs/benchmark
      :version     +version+
      :description "A benchmarking library. As used on jsPerf.com."
      :url         "https://benchmarkjs.com/"
      :scm         {:url "https://github.com/bestiejs/benchmark.js"}
      :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package []
  (comp
   (download  :url      (format "https://github.com/bestiejs/benchmark.js/archive/%s.zip" +lib-version+)
              :checksum "8768bd50e5153ff5ca00ccab147f9e1d"
              :unzip    true)
   (sift      :move     {#"^benchmark(.*)/benchmark.js"
                         "cljsjs/benchmark/development/benchmark.inc.js"})
    (minify :in "cljsjs/benchmark/development/benchmark.inc.js"
            :out "cljsjs/benchmark/production/benchmark.min.inc.js")
   (sift      :include  #{#"^cljsjs"})
   (deps-cljs :name     "cljsjs.benchmark"
              :requires ["cljsjs.lodash"
                         "cljsjs.platform"])
   (pom)
   (jar)))
