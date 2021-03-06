(ns fwpd.core)

(def filename "suspects.csv")
(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [min-glitter records]
  (filter #(>= (:glitter-index %) min-glitter) records))

(defn filter-names
  [suspects]
  (map #(:name %) suspects))

(defn append
  "Adds a new suspect to the list of suspects"
  [new-suspect list-of-suspects]
  (conj list-of-suspects new-suspect))

(defn validate-name
  [x]
  (contains? x :name))

(defn validate-glitter-index
  [x]
  (contains? x :glitter-index))

(def validations {:name validate-name
                  :glitter-index validate-glitter-index})

(defn validate
  "Validates that :name and :glitter-index are present when appending new suspect"
  [validations record]
  (and ((get validations :name) record)
       ((get validations :glitter-index) record)))

