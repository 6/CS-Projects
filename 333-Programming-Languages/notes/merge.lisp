;;; A functional version of merge sort.

(defun nrand (n max)
	"Generate n random numbers in the range [0 max-1]"
	(if (= n 0)
		'()
		(cons (random max) (nrand (1- n) max)) ))

(print (nrand 10 100))



(defun smerge (left right)
	"Merge elements from two sorted lists,
	 maintaining sorted ordering (clisp defines merge)"
	(cond 
		((eq left '()) (copy-list right))
		((eq right '()) (copy-list left))
		((<= (car left) (car right))
			(cons (car left) (smerge (cdr left) right)) )
		(t (cons (car right) (smerge left (cdr right)))) ))

(print (smerge '(1 3 5 7) '(2 4 6)))



(defun split (data)
	"Split a list into two halves"
	(labels ((split-util (left right count)
			(if (= count 0)
				(list left right)
				(split-util (append left (list (car right))) (cdr right) (1- count)) )))
		(split-util '() data (ceiling (/ (length data) 2))) ))

(print (split '(1 2 3 4 5)))



(defun merge-sort (data)
	"Merge sort"
	(if (or (null data) (null (cdr data)))
		data
		(let ((lrdata (split data)))
			(smerge 
				(merge-sort (car lrdata)) 
				(merge-sort (cadr lrdata)) ))))

(let ((data (nrand 10 100)))
	(print data)
	(print (merge-sort data)) )