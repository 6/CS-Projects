; CS333 Peter Graham
; Program for computing the n-th fibonacci number using brute-force algorithm

(defn fib [n]
  ^{:doc "Computes the n-th fibonacci number (assumes n >= 0)"}
  (if (<= n 2)
    1
    (+ (fib (- n 1)) (fib (- n 2)))))

(let [n (Integer/parseInt (first *command-line-args*))]
  (println (str "fib(" n ") = " (fib n))))