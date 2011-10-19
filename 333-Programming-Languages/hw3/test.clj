(defn change [word match newWord]
  (if (.equals word match)
    newWord
    word))

(defn split_thing [sentence]
  (doseq [word (.split sentence " ")]
    (println (change word "Pedro" "Doofus"))))

(split_thing "Hello! me llamo is Pedro.")
