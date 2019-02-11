(ns dactyl-keyboard.lightcycle
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [dactyl-keyboard.util :refer :all]
            [unicode-math.core :refer :all]))
(defn deg2rad [degrees]
  (* (/ degrees 180) pi))

;; TODO
;; work on adding functions in where I have spilt code
;; -- wrist rest layouts
;; -- bottom case thickness
;; -- ergo changes
;;
;; Create a function for the bottom case wall size to allow for cutting a
;; slightly bigger case size

;;;;;;;;;;;;;;;;;
;;; Variables ;;;
;;;;;;;;;;;;;;;;;

;; General
(def row-size 0)              ;; 0 for dactyl, 1 for lightcycle- MAKE SURE
                              ;; alpha_ergo_style is 0 for lightcycle
(def switch-type 1)           ;; 0= box 1=cherry 2= Alps -88
(def bottom-side-width 1.2)   ;; Default 1.2 Originally 1 Width of the bottom
                              ;; sides
(def top-case-thickness 1.25) ;; Default 1.25 Originally 1
(def sidewall-height 10)      ;; controls height of sidewalls on top. low
                              ;; profile Defaults 5-lightcycle 10-dactyl high
                              ;; profile
(def top-z-offset 0)          ;; Controls the z/height offset of the case.
                              ;; Default is 0 higher raises the case (dont
                              ;; forget to compensate the screw mounts)
(def top-z-offset-thumb 0)    ;; Controls thumb offset. Generally speaking
                              ;; should be the same as the z offset default 0
(def case-text '"")

;; Alpha area curve
(def front-back-curve (deg2rad 15)) ;; Default is 15 front to back curve of
                                    ;; alpha area.
(def left-right-curve (deg2rad 5))  ;; Default is 5 left-right curve of alpha
                                    ;; area.
(def alphas-column-extra-width 2.0) ;; default 2 the width between each key.
(def alphas-row-extra-width 0.5)    ;; default .5. width between keys between
                                    ;; rows
(def alpha_ergo_style 0)            ;; 0 is no ergo 1 is ergodox style--MAKE
                                    ;; SURE THIS IS 0 FOR LIGHTCYCLE


;; Thumb cluster
(def thumb-type 1)                        ;; 0=5x 2/1.25u (ie lightcycle default)
                                          ;; 1=8x1U
                                          ;; 2=2x2u(default)
(def additional-thumb-x-offset 0)         ;; additional offset for thumb
                                          ;; cluster-0 by default
(def thumb-front-back-curve (deg2rad 25)) ;; default 15 Key curve
(def thumb-left-right-curve (deg2rad 5))  ;; default 5 Key curve
(def thumb-extra-width-column 2)          ;; default 2
(def left-right-thumb-tilt 15)            ;; default 15 degrees Serious down
                                          ;; tilt =-45 which is about the limit
                                          ;; this can go
(def front-back-thumb-tilt 27)            ;; default 14 for better bottom fit
                                          ;; originally 15
(def  thumb-extra-width-row 1)            ;; default 1
(def top-z-offset-thumb 0)                ;; The z offset of the thumb cluster.
                                          ;; Seperate from main offset
(def z-thumb-rotation (deg2rad 3.5))      ;; default of 3.5


;; Wrist rest
(def wrist-rest-on 1)             ;; 0 for no rest 1 for a rest connection cut
                                  ;; out in bottom case
(def wrist-rest-back-height 46)   ;; Default 46 height of the back of the wrist
                                  ;; rest
(def wrist-rest-angle 20)         ;; Default 20 angle of the wrist rest
(def wrist-rest-rotation-angle 0) ;; Default 9 The angle in counter clockwise
                                  ;; the wrist rest is at
(def wrist-rest-ledge 4)          ;; Default 4 The height of ledge the silicone
                                  ;; wrist rest fits inside
(def rest-offset-x 0)             ;; Default 0 offset of the wrist rest in the x
                                  ;; direction
(def rest-offset-y 0)             ;; Default 0
(def wrist-rest-y-angle 0)        ;; 0 Default. Controls the wrist rest y axis
                                  ;; tilt (left to right)



;;; Should not need to modify
(def scale-bottom 1.0) ;; scale the dimensions of the bottom--dont touch this




(def ncols 6)
;; hack to work with the lightcycle and normal. This hack spiraled a bit as
;; adding the ergodox feature broke the lighcycle. This if statement fixes it
(def nrows (if (= row-size 0) (- 6 row-size) (- 7 row-size)))

;; Not needed for testing only
(def fixed-angles [(deg2rad 10) (deg2rad 10) 0 0 0 (deg2rad -15) (deg2rad -15)])
(def fixed-x [-41.5 -22.5 0 20.3 41.4 65.5 89.6])  ; relative to the middle finger
(def fixed-z [12.1    8.3 0  5   10.7 14.5 17.5])
(def fixed-tenting  0)

(def centerrow (- nrows 3))      ;; controls front-back tilt
(def tenting-angle (deg2rad 15)) ;; or, change this for more precise tenting
                                 ;; control
(def column-style
  (if (> nrows 5) :orthographic :standard)) ;; options
                                            ;; include :standard, :orthographic,
                                            ;; and :fixed
;; (def column-style :fixed)

;; needs to be reworked-- height breaks thumbs
(def centercol 3) ;; controls left-right tilt / tenting (higher number is more
                  ;; tenting)
(defn column-offset [column] (cond
                               (= column 2) [0 2.82 -4.5]
                               (>= column 4) [0 -12 5.64] ;; original [0 -5.8
                                                          ;; 5.64]
                               :else [0 0 0]))



;;;;;;;;;;;;;;;;;;;
;;; Switch Hole ;;;
;;;;;;;;;;;;;;;;;;;

(def keyswitch-height 14.15) ;; Was 14.1, then 14.25
(def keyswitch-width 14.65)  ;; Nub side original 14.5 last 14.8----14.65 works
                             ;; for both. box slightly loose

(def cherry-keyswitch-height 14.4) ;; Was 14.1, then 14.25
(def cherry-keyswitch-width 14.4)

(def alps-keyswitch-height 12.9) ;; Was 14.1, then 14.25
(def alps-keyswitch-width 15.5)
(def alps-width 15.55)
(def alps-notch-width 15.48)
(def alps-notch-height 1)
(def alps-height 12.85)


(def sa-profile-key-height 12.7)

(def plate-thickness 4)
(def mount-width (+ keyswitch-width 3))
(def mount-height (+ keyswitch-height 3))

;; kalih box
(def box-single-plate
  (let [top-wall (->> (cube (+ keyswitch-width 3) 1.5 plate-thickness)
                      (translate [0
                                  (+ (/ 1.5 2) (/ keyswitch-height 2))
                                  (/ plate-thickness 2)]))
        left-wall (->> (cube 1.5 (+ keyswitch-height 3) plate-thickness)
                       (translate [(+ (/ 1.5 2) (/ keyswitch-width 2))
                                   0
                                   (/ plate-thickness 2)]))
        side-nub (->> (binding [*fn* 30] (cube 0.7 0.85 8.75));last number is nub size.  4.75 works for box
                      (rotate (/ π 2) [1 0 0])
                      (translate [(+ (/ keyswitch-width 2)) 0 3.1]) ;last number control nub height
                      (hull (->> (cube 1.5 2.75 1)
                                 (translate [(+ (/ 1.5 2) (/ keyswitch-width 2))
                                             0
                                             (/ plate-thickness 1.15)])))
                      ) ;; 2nd number controls slant height position
        plate-half (union top-wall left-wall (with-fn 100 side-nub))]
    (union plate-half
           (->> plate-half
                (mirror [1 0 0])
                (mirror [0 1 0])))))

;; Cherry
(def cherry-single-plate
  (let [top-wall (->> (cube (+ cherry-keyswitch-width 3) 1.5 plate-thickness)
                      (translate [0
                                  (+ (/ 1.5 2) (/ cherry-keyswitch-height 2))
                                  (/ plate-thickness 2)]))
        left-wall (->> (cube 1.6 (+ cherry-keyswitch-height 3) plate-thickness)
                       (translate [(+ (/ 1.5 2) (/ cherry-keyswitch-width 2))
                                   0
                                   (/ plate-thickness 2)]))
        side-nub (->> (binding [*fn* 30] (cylinder 1 2.75))
                      (rotate (/ π 2) [1 0 0])
                      (translate [(+ (/ cherry-keyswitch-width 2)) 0 1])
                      (hull (->> (cube 1.5 2.75 plate-thickness)
                                 (translate [(+ (/ 1.5 2) (/ cherry-keyswitch-width 2))
                                             0
                                             (/ plate-thickness 2)]))))
        plate-half (union top-wall left-wall (with-fn 100 side-nub))]
    (union plate-half
           (->> plate-half
                (mirror [1 0 0])
                (mirror [0 1 0])))))
;; Matias
(def Matias-single-plate
  (let [top-wall (->> (cube (+ alps-keyswitch-width 3) 2.2 plate-thickness)
                      (translate [0
                                  (+ (/ 2.4 2) (/ alps-height 2));;changed the first number from 2.2 to 2.4 for thumb cluster
                                  (/ plate-thickness 2)]))
        left-wall (union (->> (cube 1.5 (+ alps-keyswitch-height 3) plate-thickness)
                              (translate [(+ (/ 1.5 2) (/ 15.6 2))
                                          0
                                          (/ plate-thickness 2)]))
                         (->> (cube 1.5 (+ alps-keyswitch-height 3) 1.0)
                              (translate [(+ (/ 1.5 2) (/ alps-notch-width 2))
                                          0
                                          (- plate-thickness
                                             (/ alps-notch-height 2))]))
                         )
        plate-half (union top-wall left-wall)]
    (union plate-half
           (->> plate-half
                (mirror [1 0 0])
                (mirror [0 1 0])))))

(def single-plate-rotated
  (let [top-wall (->> (cube (+ alps-keyswitch-height 3) 1.5 plate-thickness)
                      (translate [0
                                  (+ (/ 1.5 2) (/ alps-keyswitch-width 2))
                                  (/ plate-thickness 2)]))
        left-wall (->> (cube 2.8 (+ alps-keyswitch-width 3) plate-thickness)
                       (translate [(+ (/ 2.8 2) (/ alps-keyswitch-height 2))
                                   0
                                   (/ plate-thickness 2)]))

        plate-half (union top-wall left-wall )]
    (union plate-half
           (->> plate-half
                (mirror [1 0 0])
                (mirror [0 1 0])))))

(def ergo-single-plate
  (let [top-wall (->> (cube (+ keyswitch-width 3) 1.5 plate-thickness)
                      (translate [0
                                  (+ (/ 1.5 2) (/ keyswitch-height 2))
                                  (/ plate-thickness 2)]))
        left-wall (->> (cube 6.5 (+ keyswitch-height 3) plate-thickness)
                       (translate [(+ (/ 6.5 2) (/ keyswitch-width 2))
                                   0
                                   (/ plate-thickness 2)]))
        side-nub (->> (binding [*fn* 30] (cube 0.7 0.85 8.75));last number is nub size.  4.75 works for box
                      (rotate (/ π 2) [1 0 0])
                      (translate [(+ (/ keyswitch-width 2)) 0 3.1]) ;last number control nub height
                      (hull (->> (cube 1.5 2.75 1)
                                 (translate [(+ (/ 1.5 2) (/ keyswitch-width 2))
                                             0
                                             (/ plate-thickness 1.15)])))
                      );2nd number controls slant height position
        plate-half (union top-wall left-wall (with-fn 100 side-nub))]
    (union plate-half
           (->> plate-half
                (mirror [1 0 0])
                (mirror [0 1 0])))))

(def single-plate
  (if (== switch-type 0) box-single-plate
      (if (== switch-type 1) cherry-single-plate
          (if (== switch-type 2) Matias-single-plate )))
  )

;;;;;;;;;;;;;;;;;;
;;; SA Keycaps ;;;
;;;;;;;;;;;;;;;;;;

(def sa-length 18.40)
(def sa-double-length 37.5)
(def sa-cap {1 (let [bl2 (/ 18.5 2)
                     m (/ 17 2)
                     key-cap (hull (->> (polygon [[bl2 bl2] [bl2 (- bl2)] [(- bl2) (- bl2)] [(- bl2) bl2]])
                                        (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                        (translate [0 0 0.05]))
                                   (->> (polygon [[m m] [m (- m)] [(- m) (- m)] [(- m) m]])
                                        (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                        (translate [0 0 6]))
                                   (->> (polygon [[6 6] [6 -6] [-6 -6] [-6 6]])
                                        (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                        (translate [0 0 12])))]
                 (->> key-cap
                      (translate [0 0 (+ 5 plate-thickness)])
                      (color [220/255 163/255 163/255 1])))
             2 (let [bl2 (/ sa-double-length 2)
                     bw2 (/ 18.25 2)
                     key-cap (hull (->> (polygon [[bw2 bl2] [bw2 (- bl2)] [(- bw2) (- bl2)] [(- bw2) bl2]])
                                        (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                        (translate [0 0 0.05]))
                                   (->> (polygon [[6 16] [6 -16] [-6 -16] [-6 16]])
                                        (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                        (translate [0 0 12])))]
                 (->> key-cap
                      (translate [0 0 (+ 5 plate-thickness)])
                      (color [127/255 159/255 127/255 1])))
             1.5 (let [bl2 (/ 18.25 2)
                       bw2 (/ 28 2)
                       key-cap (hull (->> (polygon [[bw2 bl2] [bw2 (- bl2)] [(- bw2) (- bl2)] [(- bw2) bl2]])
                                          (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                          (translate [0 0 0.05]))
                                     (->> (polygon [[11 6] [-11 6] [-11 -6] [11 -6]])
                                          (extrude-linear {:height 0.1 :twist 0 :convexity 0})
                                          (translate [0 0 12])))]
                   (->> key-cap
                        (translate [0 0 (+ 5 plate-thickness)])
                        (color [240/255 223/255 175/255 1])))})

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Placement Functions ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def columns (range 0 ncols))
(def columnsErgo (range 0 (- ncols 1)))
(def columns2 (range 0 7))
(def rows (range row-size (- nrows 1)))
(def rows2 (range row-size nrows))
;; (/ (* π left-right-thumb-tilt) 180)  left-right-curve
(def α front-back-curve)
(def β left-right-curve)
(def cap-top-height (+ plate-thickness sa-profile-key-height))
(def row-radius (+ (/ (/ (+ mount-height alphas-row-extra-width) 2)
                      (Math/sin (/ α 2)))
                   cap-top-height))
(def column-radius (+ (/ (/ (+ mount-width alphas-column-extra-width) 2)
                         (Math/sin (/ β 2)))
                      cap-top-height))

(defn key-place [column row shape]
  (let [row-placed-shape (->> shape
                              (translate [0 0 (- row-radius)])
                              (rotate (* α (- 2 row)) [1 0 0])
                              (translate [0 0 row-radius]))
        column-offset (cond
                        (= column 2) [0 2.82 -3.0] ;; was moved -4.5----Controls
                                                   ;; the Z offset of the
                                                   ;; columns to each other
                        (>= column 4) [0 -5.8 5.64]
                        :else [0 0 0])
        column-angle (* β (- 2 column))
        placed-shape (->> row-placed-shape
                          (translate [0 0 (- column-radius)])
                          (rotate column-angle [0 1 0])
                          (translate [0 0 column-radius])
                          (translate column-offset))]
    (->> placed-shape
         (rotate (/ π 12) [0 1 0])
         (translate [0 0 (+ 13 top-z-offset)]))))

(defn case-place [column row shape]
  (let [row-placed-shape (->> shape
                              (translate [0 0 (- row-radius)])
                              (rotate (* α (- 2 row)) [1 0 0])
                              (translate [0 0 row-radius]))
        column-offset [0 -4.35 5.64]    ;; Controls offset at bottom between
                                        ;; case wall and first key
        column-angle (* β (- 2 column))
        placed-shape (->> row-placed-shape
                          (translate [0 0 (- column-radius)])
                          (rotate column-angle [0 1 0])
                          (translate [0 0 column-radius])
                          (translate column-offset))]
    (->> placed-shape
         (rotate (/ π 12) [0 1 0])
         (translate [0 0 (+ 13 top-z-offset)]))))

(def key-holes
  (apply union
         (for [column columns
               row rows
               :when (or (not= column 0)
                         (not= row 4))]
           (->> single-plate
                (key-place column row)))))

(def caps
  (apply union
         (for [column columns
               row rows
               :when (or (not= column 0)
                         (not= row 4))]
           (->> (sa-cap (if (= column 5) 1 1))
                (key-place column row)))))

(if (= alpha_ergo_style 1)
  (def caps
    (apply union
           (for [column columns
                 row rows
                 :when (or (not= column 0)
                           (not= row 4))]
             (->> (sa-cap (if (= column 5) (if (= row 4) 1 1.5) 1))
                  (key-place column row)))
           ))
  )

(if (= alpha_ergo_style 1)

  (defn key-place [column row shape]  ;; for the outside pinkie column to make
                                      ;; it the ergodox style
    (let [row-placed-shape (->> shape
                                (translate [0 0 (- row-radius)])
                                (rotate (* α (- 2 row)) [1 0 0])
                                (translate [0 0 row-radius]))
          column-offset (cond
                          (= column 2) [0 2.82 -3.0] ;;was moved -4.5
                          (or (= column 4) (and (= column 5) (= row 4))) [0 -5.8 5.64]
                          ;; This is column 5, rows 0-3, which need 1.5u room for
                          ;; Ergodox cap compatibility
                          (= column 5) [5 -5.8 6.64]
                          :else [0 0 0])
          column-angle (* β (- 2 column))
          placed-shape (->> row-placed-shape
                            (translate [0 0 (- column-radius)])
                            (rotate column-angle [0 1 0])
                            (translate [0 0 column-radius])
                            (translate column-offset))]
      (->> placed-shape
           (rotate (/ π 12) [0 1 0])
           (translate [0 0 (+ 13 top-z-offset)])))) )
(if (= alpha_ergo_style 1)
  (def key-holes ;; for the outside pinkie column to make it the ergodox style
    (let [one-us (for [column (range 0 5)
                       row rows
                       :when (or (not= column 0)
                                 (not= row 4))]
                   (->> single-plate
                        (key-place column row)))
          ;; Actually "one-and-a-halfs", for col5, rows 0-3
          one-halfs (for [row (range 0 5)]
                      (->> ergo-single-plate
                           (key-place 5 row)))
          ;; And a reugular lil' fella for the bottom right corner
          bottom-corner (->> single-plate
                             (key-place 5 4))
          ;; all-the-holes (cons bottom-corner (union one-us one-halfs ))
          all-the-holes (concat one-us one-halfs)
          ]
      (apply union
             all-the-holes))))

;;;;;;;;;;;;;;;;;;;;;;
;;; Web Connectors ;;;
;;;;;;;;;;;;;;;;;;;;;;

(def web-thickness 3.5)
(def post-size 0.1)
(def web-post (->> (cube post-size post-size web-thickness)
                   (translate [0 0 (+ (/ web-thickness -2)
                                      plate-thickness)])))

(def post-adj (/ post-size 2))
(def web-post-tr (translate [(- (/ mount-width 2) post-adj) (- (/ mount-height 2) post-adj) 0] web-post))
(def web-post-tl (translate [(+ (/ mount-width -2) post-adj) (- (/ mount-height 2) post-adj) 0] web-post))
(def web-post-bl (translate [(+ (/ mount-width -2) post-adj) (+ (/ mount-height -2) post-adj) 0] web-post))
(def web-post-br (translate [(- (/ mount-width 2) post-adj) (+ (/ mount-height -2) post-adj) 0] web-post))

;; for ergodox pinkie column
(def web-post-tl2 (translate [(+ (/ mount-width -2) post-adj) (- (/ mount-height 2) (+ 1 post-adj)) 0] web-post))
(def web-post-bl2 (translate [(+ (/ mount-width -2) post-adj) (+ (/ mount-height -2) post-adj) 0] web-post))
(def web-post-br2 (translate [(- (/ mount-width 1.28) post-adj) (+ (/ mount-height -2) post-adj) 0] web-post))
(def web-post-tr2 (translate [(- (/ mount-width 1.28) post-adj) (- (/ mount-height 2) post-adj) 0] web-post))


(def connectors
  (apply union
         (concat
          ;; Row connections
          (for [column (drop-last columns)
                row rows
                :when (or (not= column 0)
                          (not= row 4))]
            (triangle-hulls
             (key-place (inc column) row web-post-tl)
             (key-place column row web-post-tr)
             (key-place (inc column) row web-post-bl)
             (key-place column row web-post-br)))

          ;; Column connections
          (for [column columns
                row (drop-last rows)
                :when (or (not= column 0)
                          (not= row 3))]
            (triangle-hulls
             (key-place column row web-post-bl)
             (key-place column row web-post-br)
             (key-place column (inc row) web-post-tl)
             (key-place column (inc row) web-post-tr)))

          ;; This makes it possible to bridge the gaps on the 1.5u keys
          (if (== alpha_ergo_style 1)
            (for [column columns2
                  row (drop-last rows)
                  :when (= column 5)
                  ]
              (triangle-hulls
               (key-place column row web-post-bl2)
               (key-place column row web-post-br2)
               (key-place column (inc row) web-post-tl2)
               (key-place column (inc row) web-post-tr2))))

          ;; Diagonal connections
          (for [column (drop-last columns)
                row (drop-last rows)
                :when (or (not= column 0)
                          (not= row 3))]
            (triangle-hulls
             (key-place column row web-post-br)
             (key-place column (inc row) web-post-tr)
             (key-place (inc column) row web-post-bl)
             (key-place (inc column) (inc row) web-post-tl))))))

;;;;;;;;;;;;;;
;;; Thumbs ;;;
;;;;;;;;;;;;;;

(defn thumb-place [column row shape]
  (let [cap-top-height (+ plate-thickness sa-profile-key-height)
        α thumb-front-back-curve ;; controls the front to back curve of thumbs
        row-radius (+ (/ (/ (+ mount-height thumb-extra-width-row) 2)
                         (Math/sin (/ α 2)))
                      cap-top-height)
        β thumb-left-right-curve ;; make negative to invert the thumb curve
                                 ;; concave to convex
        column-radius (+ (/ (/ (+ mount-width thumb-extra-width-column) 2) ;distance between thumb keyholes
                            (Math/sin (/ β 2)));elongate thumb
                         cap-top-height)
        #_(+ (/ (/ (+ pillar-width 5) 2)
                (Math/sin (/ β 2)))
             cap-top-height)]
    (->> shape
         (translate [0 0 (- row-radius)])
         (rotate (* α row) [1 0 0])
         (translate [0 0 row-radius])
         (translate [0 0 (- column-radius)])
         (rotate (* column β) [0 1 0])
         (translate [0 0 column-radius])
         (translate [mount-width 0 0])
         (rotate (* π z-thumb-rotation) [0 0 1])
         (rotate (/ (* π front-back-thumb-tilt) 180) [1 0 0])
         ;; (rotate (/ π 12) [1 1 0]) FRONT to back angle
         ;; (rotate (/ π left-right-thumb-tilt) [0 1 0]) ;left to right rotation
         (rotate (/ (* π left-right-thumb-tilt) 180) [0 1 0])
         (translate [(+ -52 additional-thumb-x-offset) -45 (+ 40 top-z-offset-thumb)]))))


;; all 1u thumb
(defn thumb-2x-column-1u [shape]
  (union (thumb-place 0 -1 shape)
         (thumb-place 0 0 shape)))

(defn thumb-2x+1-column-1u [shape]
  (union (thumb-place 1 -1 shape)
         (thumb-place 1 0 shape)
         (thumb-place 1 1 shape)))

(defn thumb-1x-column-1u [shape]
  (union (thumb-place 2 -1 shape)
         (thumb-place 2 0 shape)
         (thumb-place 2 1 shape)))

(defn thumb-layout-1u [shape]
  (union
   (thumb-2x-column-1u shape)
   (thumb-2x+1-column-1u shape)
   (thumb-1x-column-1u shape)))

(def double-plates
  (let [plate-height (/ (- sa-double-length mount-height) 2)
        top-plate (->> (cube mount-width plate-height web-thickness)
                       (translate [0 (/ (+ plate-height mount-height) 2)
                                   (- plate-thickness (/ web-thickness 2))]))
        stabilizer-cutout (union (->> (cube 14.2 3.5 web-thickness)
                                      (translate [0.5 12 (- plate-thickness (/ web-thickness 2))])
                                      (color [1 0 0 1/2]))
                                 (->> (cube 16 3.5 web-thickness)
                                      (translate [0.5 12 (- plate-thickness (/ web-thickness 2) 1.4)])
                                      (color [1 0 0 1/2])))
        top-plate (difference top-plate)]; stabilizer-cutout)]
    (union top-plate (mirror [0 1 0] top-plate))))

(def thumbcaps-1u
  (union
   (thumb-2x-column-1u (sa-cap 1))
   (thumb-2x+1-column-1u  (sa-cap 1))
   ;; (thumb-place 1 1 (sa-cap 1))
   (thumb-1x-column-1u (sa-cap 1))
   ))

(def thumb-connectors-1u
  (union
   (apply union
          (concat
           ;; connects top singles
           (for [column [2] row [-1 0 1]]
             (triangle-hulls (thumb-place column row web-post-br)
                             (thumb-place column row web-post-tr)
                             (thumb-place (dec column) row web-post-bl)
                             (thumb-place (dec column) row web-post-tl)))

           (for [column [1] row [-1 0]]
             (triangle-hulls (thumb-place column row web-post-br)
                             (thumb-place column row web-post-tr)
                             (thumb-place (dec column) row web-post-bl)
                             (thumb-place (dec column) row web-post-tl)))

           ;; connects up and down singles
           (for [column [1 2] row [0 1]]
             (triangle-hulls
              (thumb-place column row web-post-bl)
              (thumb-place column row web-post-br)
              (thumb-place column (dec row) web-post-tl)
              (thumb-place column (dec row) web-post-tr)))
           ;; connects old 2u converted to 1u up and down.
           (for [column [0] row [0 ]]
             (triangle-hulls;;;connects up and down singles
              (thumb-place column row web-post-bl)
              (thumb-place column row web-post-br)
              (thumb-place column (dec row) web-post-tl)
              (thumb-place column (dec row) web-post-tr)))
           ))

   ;; This needs to be changed to remove the double length ie just sa-length. after
   ;; doing this I will need to modify how the triangles are made.
   (let [plate-height (/ (- sa-double-length mount-height) 2)
         thumb-tl (->> web-post-tl
                       (translate [0 plate-height 0]))
         thumb-bl (->> web-post-bl
                       (translate [0 (- plate-height) 0]))
         thumb-tr (->> web-post-tr
                       (translate [0 plate-height 0]))
         thumb-br (->> web-post-br
                       (translate [0 (- plate-height) 0]))]

     (union
      ;; Corner pieces to connect meeting of 4
      (hull (thumb-place 1 -1 thumb-tl)
            (thumb-place 1 0 thumb-bl)
            (thumb-place 2 0 web-post-br)
            (thumb-place 2 -1 web-post-tr))



      (hull (thumb-place 0 -1 thumb-tl)
            (thumb-place 0 0 thumb-bl)
            (thumb-place 1 0 web-post-br)
            (thumb-place 1 -1 web-post-tr))
      (hull (thumb-place 1 0 thumb-tl)
            (thumb-place 1 1 thumb-bl)
            (thumb-place 2 1 web-post-br)
            (thumb-place 2 0 web-post-tr))

      ( triangle-hulls (thumb-place 0 -1/2 thumb-br)
       (key-place 1 4 web-post-bl)
       (thumb-place 0 -1/2 thumb-tr)
       (key-place 1 4 web-post-tl)
       (key-place 1 3 web-post-bl)
       (thumb-place 0 -1/2 thumb-tr)
       (key-place 0 3 web-post-br)
       (key-place 0 3 web-post-bl)
       (thumb-place 0 -1/2 thumb-tr)
       (thumb-place 0 -1/2 thumb-tl)
       (key-place 0 3 web-post-bl)
       (thumb-place 1 -1/2 thumb-tr)
       (thumb-place 1 1 web-post-br)
       (key-place 0 3 web-post-bl)
       (key-place 0 3 web-post-tl)
       (thumb-place 1 1 web-post-br)
       (thumb-place 1 1 web-post-tr))


      ;; Connecting the thumb to everything 0 -1/2 double-plates
      #_  (triangle-hulls (thumb-place 0 -1/2 thumb-br)
                          (key-place 1 4 web-post-bl)
                          (thumb-place 0 -1/2 thumb-tr)

                          (key-place 1 4 web-post-tl)
                          (key-place 1 3 web-post-bl)
                          (thumb-place 0 -1/2 thumb-tr)

                          (key-place 0 3 web-post-br)
                          (key-place 0 3 web-post-bl)
                          (thumb-place 0 -1/2 thumb-tr)

                          (thumb-place 0 -1/2 thumb-tl)
                          (key-place 0 3 web-post-bl)
                          (thumb-place 1 -1 thumb-tr)

                          (thumb-place 1 1 web-post-br)
                          (key-place 0 3 web-post-bl)
                          (key-place 0 3 web-post-tl)

                          (thumb-place 1 1 web-post-br)
                          (thumb-place 1 1 web-post-tr)
                          )

      (hull (thumb-place 0 -1 web-post-tr)
            (thumb-place 0 -1/2 thumb-tr)
            (key-place 1 4 web-post-bl)
            (key-place 1 4 web-post-tl))
      ))))

;; LightCycle Thumb
(defn thumb-2x-column [shape]

  (if (> switch-type 1) (thumb-place 0 -1/2  (rotate (/ π 2) [0 0 1]shape))
      (if (< switch-type 2)(thumb-place 0 -1/2  shape))))


(defn thumb-2x+1-column [shape]
  (union (thumb-place 1 -1/2 )
         (thumb-place 1 1 shape)))

(defn thumb-1x-column [shape]
  (union (thumb-place 2 -1 shape)
         (thumb-place 2 0 shape)
         (thumb-place 2 1 shape)))



(= (* 2 sa-length)
   sa-double-length)
(def double-plates
  (let [plate-height (/ (- sa-double-length mount-height) 2)
        top-plate (->> (cube mount-width plate-height web-thickness)
                       (translate [0 (/ (+ plate-height mount-height) 2)
                                   (- plate-thickness (/ web-thickness 2))]))
        stabilizer-cutout (union (->> (cube 14.2 3.5 web-thickness)
                                      (translate [0.5 12 (- plate-thickness (/ web-thickness 2))])
                                      (color [1 0 0 1/2]))
                                 (->> (cube 16 3.5 web-thickness)
                                      (translate [0.5 12 (- plate-thickness (/ web-thickness 2) 1.4)])
                                      (color [1 0 0 1/2])))
        ;; top-plate (difference top-plate stabilizer-cutout)
        ]
    (color [1 0 0] (union top-plate (mirror [0 1 0] top-plate)))))

(defn extended-plate-height [size] (/ (- (* (+ 1 sa-length) size) mount-height) 2))

(defn extended-plates [size]
  (let [plate-height (extended-plate-height size)
        top-plate (->> (cube mount-width plate-height web-thickness)
                       (translate [0 (/ (+ plate-height mount-height) 2)
                                   (- plate-thickness (/ web-thickness 2))]))]
    (color [0 1 1] (union top-plate (mirror [0 1 0] top-plate)))
    ))

(defn thumb-layout [shape]
  (union
   (thumb-place 0 -1/2 (union shape (extended-plates 2)))

   (thumb-place 1 7/8 (union shape (extended-plates 1.25)))
   (thumb-place 1 -5/8 (union shape (extended-plates 1.75)))

   (thumb-place 2 -3/4 (union shape (extended-plates 1.5)))
   (thumb-place 2 3/4 (union shape (extended-plates 1.5)))
   ))

(defn thumb-layout-bottom [shape]
  (union
   (thumb-place 0 -1/2 shape)

   (thumb-place 1 7/8 shape)
   (thumb-place 1 -5/8 shape)

   (thumb-place 2 -3/4 shape)
   (thumb-place 2 3/4 shape)
   ))

(def thumbcaps-lightcycle
  (union
   (thumb-2x-column (sa-cap 1.5))
   ;; (thumb-layout-bottom (sa-cap 1.5))
   (thumb-place 1 -1/2 (sa-cap 2))
   (thumb-place 1 1 (sa-cap 1))
   (thumb-1x-column (sa-cap 1))
   ))

(def thumb-connectors
  (union
   #_(apply union
            (concat
             (for [column [2] row [1]]
               (triangle-hulls (thumb-place column row web-post-br)
                               (thumb-place column row web-post-tr)
                               (thumb-place (dec column) row web-post-bl)
                               (thumb-place (dec column) row web-post-tl)))
             (for [column [2] row [0 1]]
               (triangle-hulls
                (thumb-place column row web-post-bl)
                (thumb-place column row web-post-br)
                (thumb-place column (dec row) web-post-tl)
                (thumb-place column (dec row) web-post-tr)))))
   (let [thumb-tl #(->> web-post-tl
                        (translate [0 (extended-plate-height %) 0]))
         thumb-bl #(->> web-post-bl
                        (translate [0 (- (extended-plate-height %)) 0]))
         thumb-tr #(->> web-post-tr
                        (translate [0 (extended-plate-height %) 0]))
         thumb-br #(->> web-post-br
                        (translate [0 (- (extended-plate-height %)) 0]))]
     (union

      ;; Connecting the double to 1.75
      (triangle-hulls (thumb-place 0 -1/2 (thumb-tl 2))
                      (thumb-place 0 -1/2 (thumb-bl 2))
                      (thumb-place 1 -5/8 (thumb-br 1.75))
                      (thumb-place 0 -1/2 (thumb-tl 2))
                      (thumb-place 1 -5/8 (thumb-tr 1.75))
                      (thumb-place 1 7/8 (thumb-br 1.25)))

      (triangle-hulls (thumb-place 1 7/8 (thumb-br 1.25))
                      (thumb-place 1 7/8 (thumb-bl 1.25))
                      (thumb-place 1 -5/8 (thumb-tr 1.75))
                      (thumb-place 1 -5/8 (thumb-tl 1.75)))

      (triangle-hulls (thumb-place 2 3/4 (thumb-br 1.5))
                      (thumb-place 2 3/4 (thumb-bl 1.5))
                      (thumb-place 2 -3/4 (thumb-tr 1.5))
                      (thumb-place 2 -3/4 (thumb-tl 1.5)))

      (triangle-hulls (thumb-place 2 3/4 (thumb-br 1.5))
                      (thumb-place 2 3/4 (thumb-bl 1.5))
                      (thumb-place 2 -3/4 (thumb-tr 1.5))
                      (thumb-place 2 -3/4 (thumb-tl 1.5)))

      (triangle-hulls (thumb-place 2 -3/4 (thumb-br 1.5))
                      (thumb-place 1 -5/8 (thumb-bl 1.75))
                      (thumb-place 2 -3/4 (thumb-tr 1.5))
                      (thumb-place 1 -5/8 (thumb-tl 1.75))
                      (thumb-place 2 3/4 (thumb-br 1.5))
                      (thumb-place 1 7/8 (thumb-bl 1.25))
                      (thumb-place 2 3/4 (thumb-tr 1.5))
                      (thumb-place 1 7/8 (thumb-tl 1.25))
                      )


      ;; Connecting the thumb to everything
      (triangle-hulls (thumb-place 0 -1/2 (thumb-br 2))
                      (key-place 1 4 web-post-bl)
                      (thumb-place 0 -1/2 (thumb-tr 2))
                      (key-place 1 4 web-post-tl)
                      (key-place 1 3 web-post-bl)
                      (thumb-place 0 -1/2 (thumb-tr 2))
                      (key-place 0 3 web-post-br)
                      (key-place 0 3 web-post-bl)
                      (thumb-place 0 -1/2 (thumb-tr 2))
                      (thumb-place 0 -1/2 (thumb-tl 2))
                      (key-place 0 3 web-post-bl)
                      (thumb-place 1 -5/8 (thumb-tr 1.75))
                      (thumb-place 1 7/8 (thumb-br 1.25))
                      (key-place 0 3 web-post-bl)
                      (key-place 0 3 web-post-tl)
                      (thumb-place 1 7/8 (thumb-br 1.25))
                      (thumb-place 1 7/8 (thumb-tr 1.25))
                      )))))

;; 2U
(defn thumb-2x-column-2u [shape]
  (if (> switch-type 1) (thumb-place 0 -1/2  (rotate (/ π 2) [0 0 1]shape))
      (if (< switch-type 2)(thumb-place 0 -1/2  shape))))



(defn thumb-2x+1-column-2u [shape]
  (union (if (> switch-type 1) (thumb-place 1 -1/2  (rotate (/ π 2) [0 0 1]shape))
             (if (< switch-type 2)(thumb-place 1 -1/2  shape)))
         (thumb-place 1 1 shape)))

(defn thumb-1x-column-2u [shape]
  (union (thumb-place 2 -1 shape)
         (thumb-place 2 0 shape)
         (thumb-place 2 1 shape)))

(defn thumb-layout-2u [shape]
  (union
   (thumb-2x-column-2u shape)
   (thumb-2x+1-column-2u shape)
   (thumb-1x-column-2u shape)))

(def double-plates
  (let [plate-height (/ (- sa-double-length mount-height) 2)
        top-plate (->> (cube mount-width plate-height web-thickness)
                       (translate [0 (/ (+ plate-height mount-height) 2)
                                   (- plate-thickness (/ web-thickness 2))]))
        stabilizer-cutout (union (->> (cube 14.2 3.5 web-thickness)
                                      (translate [0.5 12 (- plate-thickness (/ web-thickness 2))])
                                      (color [1 0 0 1/2]))
                                 (->> (cube 16 3.5 web-thickness)
                                      (translate [0.5 12 (- plate-thickness (/ web-thickness 2) 1.4)])
                                      (color [1 0 0 1/2])))
        top-plate (difference top-plate stabilizer-cutout)]
    (union top-plate (mirror [0 1 0] top-plate))))

(def thumbcaps-2u
  (union
   (thumb-2x-column-2u (sa-cap 2))
   (thumb-place 1 -1/2 (sa-cap 2))
   (thumb-place 1 1 (sa-cap 1))
   (thumb-1x-column-2u (sa-cap 1))))

(def thumb-connectors-2u
  (union
   (apply union
          (concat
           (for [column [2] row [1]]
             (triangle-hulls (thumb-place column row web-post-br)
                             (thumb-place column row web-post-tr)
                             (thumb-place (dec column) row web-post-bl)
                             (thumb-place (dec column) row web-post-tl)))
           (for [column [2] row [0 1]]
             (triangle-hulls
              (thumb-place column row web-post-bl)
              (thumb-place column row web-post-br)
              (thumb-place column (dec row) web-post-tl)
              (thumb-place column (dec row) web-post-tr)))))
   (let [plate-height (/ (- sa-double-length mount-height) 2)
         thumb-tl (->> web-post-tl
                       (translate [0 plate-height 0]))
         thumb-bl (->> web-post-bl
                       (translate [0 (- plate-height) 0]))
         thumb-tr (->> web-post-tr
                       (translate [0 plate-height 0]))
         thumb-br (->> web-post-br
                       (translate [0 (- plate-height) 0]))]
     (union

      ;; Connecting the two doubles
      (triangle-hulls (thumb-place 0 -1/2 thumb-tl)
                      (thumb-place 0 -1/2 thumb-bl)
                      (thumb-place 1 -1/2 thumb-tr)
                      (thumb-place 1 -1/2 thumb-br))

      ;; Connecting the double to the one above it
      (triangle-hulls (thumb-place 1 -1/2 thumb-tr)
                      (thumb-place 1 -1/2 thumb-tl)
                      (thumb-place 1 1 web-post-br)
                      (thumb-place 1 1 web-post-bl))

      ;; Connecting the 4 with the double in the bottom left
      (triangle-hulls (thumb-place 1 1 web-post-bl)
                      (thumb-place 1 -1/2 thumb-tl)
                      (thumb-place 2 1 web-post-br)
                      (thumb-place 2 0 web-post-tr))

      ;; Connecting the two singles with the middle double
      (hull (thumb-place 1 -1/2 thumb-tl)
            (thumb-place 1 -1/2 thumb-bl)
            (thumb-place 2 0 web-post-br)
            (thumb-place 2 -1 web-post-tr))
      (hull (thumb-place 1 -1/2 thumb-tl)
            (thumb-place 2 0 web-post-tr)
            (thumb-place 2 0 web-post-br))
      (hull (thumb-place 1 -1/2 thumb-bl)
            (thumb-place 2 -1 web-post-tr)
            (thumb-place 2 -1 web-post-br))

      ;; Connecting the thumb to everything
      (triangle-hulls (thumb-place 0 -1/2 thumb-br)
                      (key-place 1 4 web-post-bl)
                      (thumb-place 0 -1/2 thumb-tr)
                      (key-place 1 4 web-post-tl)
                      (key-place 1 3 web-post-bl)
                      (thumb-place 0 -1/2 thumb-tr)
                      (key-place 0 3 web-post-br)
                      (key-place 0 3 web-post-bl)
                      (thumb-place 0 -1/2 thumb-tr)
                      (thumb-place 0 -1/2 thumb-tl)
                      (key-place 0 3 web-post-bl)
                      (thumb-place 1 -1/2 thumb-tr)
                      (thumb-place 1 1 web-post-br)
                      (key-place 0 3 web-post-bl)
                      (key-place 0 3 web-post-tl)
                      (thumb-place 1 1 web-post-br)
                      (thumb-place 1 1 web-post-tr))
      (hull (thumb-place 0 -1/2 web-post-tr)
            (thumb-place 0 -1/2 thumb-tr)
            (key-place 1 4 web-post-bl)
            (key-place 1 4 web-post-tl))))))

;;; this is where the thumb is called based on what is selected in variables section.

(def thumb-lightcycle
  (union
   thumb-connectors


   (if (> switch-type 1) (thumb-layout (rotate (/ π 2) [0 0 1] single-plate)))
   (if (< switch-type 2)(thumb-layout (rotate (/ π 1) [0 0 1] single-plate)))
   ))
(def thumb-1u
  (union
   thumb-connectors-1u
   (thumb-layout-1u (rotate (/ π 1) [0 0 1] single-plate))
   ))
(def thumb-2u
  (union
   thumb-connectors-2u
   (thumb-layout-2u (rotate (/ π 1) [0 0 1] single-plate))
   (thumb-place 0 -1/2 double-plates)
   (thumb-place 1 -1/2 double-plates)
   ))
(def thumb
  (if (== thumb-type 0) thumb-lightcycle
      (if (== thumb-type 1) thumb-1u
          (if (== thumb-type 2) thumb-2u  )))
  )
(def thumbcaps
  (if (== thumb-type 0) thumbcaps-lightcycle
      (if (== thumb-type 1) thumbcaps-1u
          (if (== thumb-type 2) thumbcaps-2u  )))
  )


;;;;;;;;;;;;;;;;;;;;;;
;;; Top Case walls ;;;
;;;;;;;;;;;;;;;;;;;;;;

;; In column units
(def right-wall-column (+ (last columns) 0.55))
(def left-wall-column (- (first columns) 1/2))
(def thumb-back-y 0.93)
(def thumb-right-wall (- -1/2 0.05))
(def thumb-front-row (+ -1 0.07))
(def thumb-left-wall-column (+ 5/2 0.05))
(def back-y (- (first rows) 0.2 ))

(if (= alpha_ergo_style 1)
  (def right-wall-column (+ (last columns) 1.05)))
;; (def back-y (+ (first rows) #_0.02 -0.15))

(defn range-inclusive [start end step]
  (concat (range start end step) [end]))

(def wall-step 0.2)
(def wall-sphere-n 20) ;; Sphere resolution, lower for faster renders

(defn wall-sphere-at [coords]
  (->> (sphere top-case-thickness)
       (translate coords)
       (with-fn wall-sphere-n)))

(defn scale-to-range [start end x]
  (+ start (* (- end start) x)))

(defn wall-sphere-bottom [front-to-back-scale]
  (wall-sphere-at [0
                   (scale-to-range
                    (+ (/ mount-height -2) -3.5)
                    (+ (/ mount-height 2) 5.0)
                    front-to-back-scale)
                   -6]))

(defn wall-sphere-top [front-to-back-scale]
  (wall-sphere-at [0
                   (scale-to-range
                    (+ (/ mount-height -2) -3.5)
                    (+ (/ mount-height 2) 3.5)
                    front-to-back-scale)
                   sidewall-height]));; controls height of sidewalls on top.

(def wall-sphere-top-back (wall-sphere-top 1))
(def wall-sphere-bottom-back (wall-sphere-bottom 1))
(def wall-sphere-bottom-front (wall-sphere-bottom 0))
(def wall-sphere-top-front (wall-sphere-top 0))

(defn top-case-cover [place-fn sphere
                      x-start x-end
                      y-start y-end
                      step]
  (apply union
         (for [x (range-inclusive x-start (- x-end step) step)
               y (range-inclusive y-start (- y-end step) step)]
           (hull (place-fn x y sphere)
                 (place-fn (+ x step) y sphere)
                 (place-fn x (+ y step) sphere)
                 (place-fn (+ x step) (+ y step) sphere)))))

(def front-wall
  (let [step wall-step ;;0.1
        wall-step 0.05 ;;0.05 Controls the overhang at the top. How much it covers forward.
        place case-place
        top-cover (fn [x-start x-end y-start y-end]
                    (top-case-cover place wall-sphere-top-front
                                    x-start x-end y-start y-end
                                    wall-step))]
    (union
     (apply union   ;;Front left top case wall
            (for [x (range-inclusive 0.7 (- right-wall-column step) step)]
              (hull (place x 4 wall-sphere-top-front)
                    (place (+ x step) 4 wall-sphere-top-front)
                    (place x 4 wall-sphere-bottom-front)
                    (place (+ x step) 4 wall-sphere-bottom-front))))
     (apply union
            (for [x (range-inclusive 0.5 0.7 0.01)]
              (hull (place x 4 wall-sphere-top-front)
                    (place (+ x step) 4 wall-sphere-top-front)
                    (place 0.7 4 wall-sphere-bottom-front))))
     (top-cover 0.5 1.7 3.6 4)
     (top-cover 1.59 2.41 3.42 4) ;; was 3.32
     (top-cover 2.39 3.41 3.6 4)
     (if (= alpha_ergo_style 1)
       (top-cover 5.59 6.00 3.0 4))
     (apply union
            (for [x (range 2 5)]
              (union
               (hull (place (- x 1/2) 4 (translate [0 1 1] wall-sphere-bottom-front))
                     (place (+ x 1/2) 4 (translate [0 1 1] wall-sphere-bottom-front))
                     (key-place x 4 web-post-bl)
                     (key-place x 4 web-post-br))
               (hull (place (- x 1/2) 4 (translate [0 1 1] wall-sphere-bottom-front))
                     (key-place x 4 web-post-bl)
                     (key-place (- x 1) 4 web-post-br)))))
     (hull (place right-wall-column 4 (translate [0 1 1] wall-sphere-bottom-front))
           (place (- right-wall-column 1) 4 (translate [0 1 1] wall-sphere-bottom-front))
           (key-place 5 4 web-post-bl)
           (key-place 5 4 web-post-br))
     (hull (place (+ 4 1/2) 4 (translate [0 1 1] wall-sphere-bottom-front))
           (place (- right-wall-column 1) 4 (translate [0 1 1] wall-sphere-bottom-front))
           (key-place 4 4 web-post-br)
           (key-place 5 4 web-post-bl))
     (hull (place 0.7 4 (translate [0 1 1] wall-sphere-bottom-front))
           (place 1.7 4 (translate [0 1 1] wall-sphere-bottom-front))
           (key-place 1 4 web-post-bl)
           (key-place 1 4 web-post-br)))))

(def back-wall
  (let [back-row (first rows)
        step wall-step
        wall-sphere-top-backtep 0.05
        place case-place
        front-top-cover (fn [x-start x-end y-start y-end]
                          (apply union
                                 (for [x (range-inclusive x-start (- x-end wall-sphere-top-backtep) wall-sphere-top-backtep)
                                       y (range-inclusive y-start (- y-end wall-sphere-top-backtep) wall-sphere-top-backtep)]
                                   (hull (place x y wall-sphere-top-back)
                                         (place (+ x wall-sphere-top-backtep) y wall-sphere-top-back)
                                         (place x (+ y wall-sphere-top-backtep) wall-sphere-top-back)
                                         (place (+ x wall-sphere-top-backtep) (+ y wall-sphere-top-backtep) wall-sphere-top-back)))))]
    (union
     (apply union
            (for [x (range-inclusive left-wall-column (- right-wall-column step) step)]
              (hull (place x back-y wall-sphere-top-back)
                    (place (+ x step) back-y wall-sphere-top-back)
                    (place x back-y wall-sphere-bottom-back)
                    (place (+ x step) back-y wall-sphere-bottom-back))))
     (front-top-cover left-wall-column 1.56 back-y (+ back-y 0.06));;The following 3 control back overhang
     (front-top-cover left-wall-column right-wall-column back-y (+ back-y 0.15))
     ;; left 4 columns overhang-- was 2.4 but had issues with alps and
     ;; lightcycle
     ;; (front-top-cover 1.56 2.44 back-y (+ back-y 0.24));middle finger column
     (front-top-cover 3.56 4.44 back-y (+ back-y 0.32));2nd from right overhand
     (front-top-cover 4.3 right-wall-column back-y (+ back-y 0.32)) ;;Edge most column


     (hull (place left-wall-column back-y (translate [1 -1 1] wall-sphere-bottom-back))
           (place (+ left-wall-column 1) back-y (translate [0 -1 1] wall-sphere-bottom-back))
           (key-place 0 back-row web-post-tl)
           (key-place 0 back-row web-post-tr))

     (hull (place 5 back-y (translate [0 -1 1] wall-sphere-bottom-back))
           ;; This is the rightmost section of the back
           (place right-wall-column back-y (translate [0 -1 1] wall-sphere-bottom-back))
           (key-place 5 back-row web-post-tl)
           (if (== alpha_ergo_style 1) (key-place 5 back-row (translate[5 0 0] web-post-tr )))
           (if (== alpha_ergo_style 0) (key-place 5 back-row web-post-tr ))
           )

     (apply union
            (for [x (range 1 5)]
              (union
               (hull (place (- x 1/2) back-y (translate [0 -1 1] wall-sphere-bottom-back))
                     (place (+ x 1/2) back-y (translate [0 -1 1] wall-sphere-bottom-back))
                     (key-place x back-row web-post-tl)
                     (key-place x back-row web-post-tr))
               (hull (place (- x 1/2) back-y (translate [0 -1 1] wall-sphere-bottom-back))
                     (key-place x back-row web-post-tl)
                     (key-place (- x 1) back-row web-post-tr)))))
     (hull (place (- 5 1/2) back-y (translate [0 -1 1] wall-sphere-bottom-back))
           (place 5 back-y (translate [0 -1 1] wall-sphere-bottom-back))
           (key-place 4 back-row web-post-tr)
           (key-place 5 back-row web-post-tl)))))

(def right-wall
  (let [place case-place]
    (union
     (apply union
            (map (partial apply hull)
                 (partition 2 1
                            (for [scale (range-inclusive 0 1 0.01)]
                              (let [x (scale-to-range 4 back-y scale)]
                                (hull (place right-wall-column x (wall-sphere-top scale))
                                      (place right-wall-column x (wall-sphere-bottom scale))))))))

     (apply union
            (concat
             (for [x (range row-size 5)]
               (union
                (hull (place right-wall-column x (translate [-1 0 1] (wall-sphere-bottom 1/2)))
                      (key-place 5 x web-post-br)
                      (key-place 5 x web-post-tr))))
             (for [x (range row-size 4)]
               (union
                (hull (place right-wall-column x (translate [-1 0 1] (wall-sphere-bottom 1/2)))
                      (place right-wall-column (inc x) (translate [-1 0 1] (wall-sphere-bottom 1/2)))
                      (key-place 5 x web-post-br)
                      (key-place 5 (inc x) web-post-tr))))
             [(union
               (hull (place right-wall-column (first rows) (translate [-1 0 1] (wall-sphere-bottom 1/2)))
                     (place right-wall-column back-y (translate [-1 -1 1] (wall-sphere-bottom 1)))
                     (key-place 5 (first rows) web-post-tr))
               (hull (place right-wall-column 4 (translate [-1 0 1] (wall-sphere-bottom 1/2)))
                     (place right-wall-column 4 (translate [-1 1 1] (wall-sphere-bottom 0)))
                     (key-place 5 4 web-post-br)))])))))

(def left-wall-dactyl
  (let [place case-place]
    (union
     (apply union
            (for [x (range-inclusive (dec (first rows)) (- 1.6666 wall-step) wall-step)]
              (hull (place left-wall-column x wall-sphere-top-front)
                    (place left-wall-column (+ x wall-step) wall-sphere-top-front)
                    (place left-wall-column x wall-sphere-bottom-front)
                    (place left-wall-column (+ x wall-step) wall-sphere-bottom-front))))
     (hull (place left-wall-column (dec (first rows)) wall-sphere-top-front)
           (place left-wall-column (dec (first rows)) wall-sphere-bottom-front)
           (place left-wall-column back-y wall-sphere-top-back)
           (place left-wall-column back-y wall-sphere-bottom-back))
     (hull (place left-wall-column 0 (translate [1 -1 1] wall-sphere-bottom-back))
           (place left-wall-column 1 (translate [1 0 1] wall-sphere-bottom-back))
           (key-place 0 0 web-post-tl)
           (key-place 0 0 web-post-bl))
     (hull (place left-wall-column 1 (translate [1 0 1] wall-sphere-bottom-back))
           (place left-wall-column 2 (translate [1 0 1] wall-sphere-bottom-back))
           (key-place 0 0 web-post-bl)
           (key-place 0 1 web-post-bl))
     (hull (place left-wall-column 2 (translate [1 0 1] wall-sphere-bottom-back))
           (place left-wall-column 1.6666  (translate [1 0 1] wall-sphere-bottom-front))
           (key-place 0 1 web-post-bl)
           (key-place 0 2 web-post-bl))
     (hull (place left-wall-column 1.6666  (translate [1 0 1] wall-sphere-bottom-front))
           (key-place 0 2 web-post-bl)
           (key-place 0 3 web-post-tl))
     (hull (place left-wall-column 1.6666  (translate [1 0 1] wall-sphere-bottom-front))
           (thumb-place 1 1 web-post-tr)
           (key-place 0 3 web-post-tl))
     (hull (place left-wall-column 1.6666 (translate [1 0 1] wall-sphere-bottom-front))
           (thumb-place 1 1 web-post-tr)
           (thumb-place 1/2 thumb-back-y (translate [0 -1 1] wall-sphere-bottom-back))))))

;;; needs modification
(def left-wall-lightcycle
  (let [place case-place]
    (union
     (apply union
            (for [x (range-inclusive (dec (first rows)) (- 1.6666 wall-step) wall-step)]
              (hull (place left-wall-column x wall-sphere-top-front)
                    (place left-wall-column (+ x wall-step) wall-sphere-top-front)
                    (place left-wall-column x wall-sphere-bottom-front)
                    (place left-wall-column (+ x wall-step) wall-sphere-bottom-front))))
     (hull (place left-wall-column (dec (first rows)) wall-sphere-top-front)
           (place left-wall-column (dec (first rows)) wall-sphere-bottom-front)
           (place left-wall-column back-y wall-sphere-top-back)
           (place left-wall-column back-y wall-sphere-bottom-back))
     ;; the below 2 need to be changed for dactyl
     #_ (color [0 1 0] (hull (place left-wall-column row-size (translate [1 -1 1] wall-sphere-bottom-back))
                             (place left-wall-column (+ row-size 1) (translate [1 0 1] wall-sphere-bottom-back))
                             (key-place 0 0 web-post-tl)
                             (key-place 0 0 web-post-bl)))

     (color [0 1 0] (hull (place left-wall-column 1 (translate [1 -1 1] wall-sphere-bottom-back))
                          (place left-wall-column 2 (translate [1 0 1] wall-sphere-bottom-back))
                          (key-place 0 1 web-post-tl)
                          (key-place 0 1 web-post-bl)))
     (color [1 0 0] (hull (place left-wall-column 2 (translate [1 0 1] wall-sphere-bottom-back))
                          (place left-wall-column 1.6666  (translate [1 0 1] wall-sphere-bottom-front))
                          (key-place 0 1 web-post-bl)
                          (key-place 0 2 web-post-bl)))
     (color [1 1 0] (hull (place left-wall-column 1.6666  (translate [1 0 1] wall-sphere-bottom-front))
                          (key-place 0 2 web-post-bl)
                          (key-place 0 3 web-post-tl)))
     (hull (place left-wall-column 1.6666  (translate [1 0 1] wall-sphere-bottom-front))
           (thumb-place 1 1 web-post-tr)
           (key-place 0 3 web-post-tl))
     (hull (place left-wall-column 1.6666 (translate [1 0 1] wall-sphere-bottom-front))
           (thumb-place 1 1 web-post-tr)
           (thumb-place 1/2 thumb-back-y (translate [0 -1 1] wall-sphere-bottom-back))))))

(def thumb-back-wall
  (let [step wall-step
        top-step 0.05
        front-top-cover (fn [x-start x-end y-start y-end]
                          (apply union
                                 (for [x (range-inclusive x-start (- x-end top-step) top-step)
                                       y (range-inclusive y-start (- y-end top-step) top-step)]
                                   (hull (thumb-place x y wall-sphere-top-back)
                                         (thumb-place (+ x top-step) y wall-sphere-top-back)
                                         (thumb-place x (+ y top-step) wall-sphere-top-back)
                                         (thumb-place (+ x top-step) (+ y top-step) wall-sphere-top-back)))))
        back-y thumb-back-y]
    (union
     (apply union
            (for [x (range-inclusive 1/2 (- (+ 5/2 0.05) step) step)]
              (hull (thumb-place x back-y wall-sphere-top-back)
                    (thumb-place (+ x step) back-y wall-sphere-top-back)
                    (thumb-place x back-y wall-sphere-bottom-back)
                    (thumb-place (+ x step) back-y wall-sphere-bottom-back))))
     (hull (thumb-place 1/2 back-y wall-sphere-top-back)
           (thumb-place 1/2 back-y wall-sphere-bottom-back)
           (case-place left-wall-column 1.6666 wall-sphere-top-front))
     (hull (thumb-place 1/2 back-y wall-sphere-bottom-back)
           (case-place left-wall-column 1.6666 wall-sphere-top-front)
           (case-place left-wall-column 1.6666 wall-sphere-bottom-front))
     (hull
      (thumb-place 1/2 thumb-back-y (translate [0 -1 1] wall-sphere-bottom-back))
      (thumb-place 1 1 web-post-tr)
      (thumb-place 3/2 thumb-back-y (translate [0 -1 1] wall-sphere-bottom-back))
      (thumb-place 1 1 web-post-tl))
     (hull
      (thumb-place (+ 5/2 0.05) thumb-back-y (translate [1 -1 1] wall-sphere-bottom-back))
      (thumb-place 3/2 thumb-back-y (translate [0 -1 1] wall-sphere-bottom-back))
      (thumb-place 1 1 web-post-tl)
      (thumb-place 2 1 web-post-tl)))))

(def thumb-left-wall
  (let [step wall-step
        place thumb-place]
    (union
     (apply union
            (for [x (range-inclusive (+ -1 0.07) (- 1.95 step) step)]
              (hull (place thumb-left-wall-column x wall-sphere-top-front)
                    (place thumb-left-wall-column (+ x step) wall-sphere-top-front)
                    (place thumb-left-wall-column x wall-sphere-bottom-front)
                    (place thumb-left-wall-column (+ x step) wall-sphere-bottom-front))))
     (hull (place thumb-left-wall-column 1.95 wall-sphere-top-front)
           (place thumb-left-wall-column 1.95 wall-sphere-bottom-front)
           (place thumb-left-wall-column thumb-back-y wall-sphere-top-back)
           (place thumb-left-wall-column thumb-back-y wall-sphere-bottom-back))

     (hull
      (thumb-place thumb-left-wall-column thumb-back-y (translate [1 -1 1] wall-sphere-bottom-back))
      (thumb-place thumb-left-wall-column 0 (translate [1 0 1] wall-sphere-bottom-back))
      (thumb-place 2 1 web-post-tl)
      (thumb-place 2 1 web-post-bl))
     (hull
      (thumb-place thumb-left-wall-column 0 (translate [1 0 1] wall-sphere-bottom-back))
      (thumb-place 2 0 web-post-tl)
      (thumb-place 2 1 web-post-bl))
     (hull
      (thumb-place thumb-left-wall-column 0 (translate [1 0 1] wall-sphere-bottom-back))
      (thumb-place thumb-left-wall-column -1 (translate [1 0 1] wall-sphere-bottom-back))
      (thumb-place 2 0 web-post-tl)
      (thumb-place 2 0 web-post-bl))
     (hull
      (thumb-place thumb-left-wall-column -1 (translate [1 0 1] wall-sphere-bottom-back))
      (thumb-place 2 -1 web-post-tl)
      (thumb-place 2 0 web-post-bl))
     (hull
      (thumb-place thumb-left-wall-column -1 (translate [1 0 1] wall-sphere-bottom-back))
      (thumb-place thumb-left-wall-column (+ -1 0.07) (translate [1 1 1] wall-sphere-bottom-front))
      (thumb-place 2 -1 web-post-tl)
      (thumb-place 2 -1 web-post-bl)))))

(def thumb-front-wall
  (let [step wall-step ;;0.1
        wall-sphere-top-fronttep 0.05 ;;0.05
        place thumb-place
        plate-height (/ (- sa-double-length mount-height) 2)
        thumb-tl (->> web-post-tl
                      (translate [0 plate-height 0]))
        thumb-bl (->> web-post-bl
                      (translate [0 (- plate-height) 0]))
        thumb-tr (->> web-post-tr
                      (translate [-0 plate-height 0]))
        thumb-br (->> web-post-br
                      (translate [-0 (- plate-height) 0]))]
    (union
     (apply union
            (for [x (range-inclusive thumb-right-wall (- (+ 5/2 0.05) step) step)]
              (hull (place x thumb-front-row wall-sphere-top-front)
                    (place (+ x step) thumb-front-row wall-sphere-top-front)
                    (place x thumb-front-row wall-sphere-bottom-front)
                    (place (+ x step) thumb-front-row wall-sphere-bottom-front))))
     (if ( > left-right-thumb-tilt -30)(->>
                                        (hull (place thumb-right-wall thumb-front-row wall-sphere-top-front)
                                              (place thumb-right-wall thumb-front-row wall-sphere-bottom-front)
                                              (case-place 0.5 4 wall-sphere-top-front))
                                        (hull (place thumb-right-wall thumb-front-row wall-sphere-bottom-front)
                                              (case-place 0.5 4 wall-sphere-top-front)
                                              (case-place 0.7 4 wall-sphere-bottom-front))))

     (hull (place thumb-right-wall thumb-front-row wall-sphere-bottom-front)
           (key-place 1 4 web-post-bl)
           (place 0 -1/2 thumb-br)
           (place 0 -1/2 web-post-br)
           (case-place 0.7 4 wall-sphere-bottom-front))

     (hull (place (+ 5/2 0.05) thumb-front-row (translate [1 1 1] wall-sphere-bottom-front))
           (place (+ 3/2 0.05) thumb-front-row (translate [0 1 1] wall-sphere-bottom-front))
           (place 2 -1 web-post-bl)
           (place 2 -1 web-post-br))

     (hull (place thumb-right-wall thumb-front-row (translate [0 1 1] wall-sphere-bottom-front))
           (place (+ 1/2 0.05) thumb-front-row (translate [0 1 1] wall-sphere-bottom-front))
           (place 0 -1/2 thumb-bl)
           (place 0 -1/2 thumb-br))
     (hull (place (+ 1/2 0.05) thumb-front-row (translate [0 1 1] wall-sphere-bottom-front))
           (place (+ 3/2 0.05) thumb-front-row (translate [0 1 1] wall-sphere-bottom-front))
           (place 0 -1/2 thumb-bl)
           (place 1 -1/2 thumb-bl)
           (place 1 -1/2 thumb-br)
           (place 2 -1 web-post-br)))))

(def new-case
  (union front-wall
         right-wall
         back-wall
         (if (== row-size 0) left-wall-dactyl
             (if (== row-size 1) left-wall-lightcycle))
    ;;;     left-wall
         thumb-back-wall
         thumb-left-wall
         thumb-front-wall))

;;;;;;;;;;;;;;;;;;;;
;;; Bottom SIDES ;;;
;;;;;;;;;;;;;;;;;;;;

(def right-wall-column-bottom (+ (last columns) 0.53))
(if (== alpha_ergo_style 1) (def right-wall-column-bottom (+ (last columns) 1.03)))

(defn bottom-wall-sphere-at [coords]
  (->> (sphere bottom-side-width)
       (translate coords)
       (with-fn wall-sphere-n)))


(def thumb-front-row-sides (+ -1 0.085))


(defn bottom-scale-to-range [start end x]
  (+ start (* (- end start) x)))

(defn bottom-wall-sphere-bottom [front-to-back-scale]
  (bottom-wall-sphere-at [0
                          (bottom-scale-to-range
                           (+ (/ mount-height -2) -3.5)
                           (+ (/ mount-height 2) 5.0)
                           front-to-back-scale)
                          -6]))
;; Controls the height of the thumb to main keyboard position. front corner

(defn bottom-wall-inner-thumb [front-to-back-scale]
  (bottom-wall-sphere-at [0
                          (bottom-scale-to-range
                           (+ (/ mount-height -2) -3.5)
                           (+ (/ mount-height 2) 5.0)
                           front-to-back-scale)
                          -11.5]))
(defn bottom-wall-inner-thumb-side [front-to-back-scale]
  (bottom-wall-sphere-at [0
                          (bottom-scale-to-range
                           (+ (/ mount-height -2) -3.5)
                           (+ (/ mount-height 2) 5.0)
                           front-to-back-scale)
                          -7.5]))

(defn bottom-wall-sphere-top [front-to-back-scale]
  (bottom-wall-sphere-at [0
                          (bottom-scale-to-range
                           (+ (/ mount-height -2) -3.5)
                           (+ (/ mount-height 2) 3.5)
                           front-to-back-scale)
                          5]))

(def bottom-wall-sphere-top-back (bottom-wall-sphere-top 1))
(def bottom-wall-sphere-bottom-back (bottom-wall-sphere-bottom 1))
(def bottom-wall-sphere-bottom-front (bottom-wall-sphere-bottom 0))
(def bottom-wall-sphere-thumb-front (bottom-wall-inner-thumb 0))
(def bottom-wall-sphere-thumb-side (bottom-wall-inner-thumb-side 0))
(def bottom-wall-sphere-top-front (bottom-wall-sphere-top 0))


(defn bottom-of-case [height p]
  (->> (project p)
       (extrude-linear {:height height :twist 0 :convexity 2})
       (translate [0 0 (- (/ height 223) 10)])))

(defn bottom-hull1 [& p]
  (hull p (bottom-of-case 0.001 p)))

(def bottom-key-guard (->> (cube mount-width mount-height web-thickness)
                           (translate [0 0 (+ (- (/ web-thickness 23)) -4.5)])))
(def bottom-front-key-guard (->> (cube mount-width (/ mount-height 2) web-thickness)
                                 (translate [0 (/ mount-height 4) (+ (- (/ web-thickness 2)) -4.5)])))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; the walls of the bottom case ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def thumb-front-wall-lower
  [(hull (bottom-hull(thumb-place (+ 5/2 0.05) thumb-front-row-sides (translate [2 2.4 1]  bottom-wall-sphere-bottom-front)))
         (bottom-hull (thumb-place (+ 3/2 0.05) thumb-front-row-sides (translate [0 2.4 1]  bottom-wall-sphere-bottom-front)))
                                        ; (thumb-place 2 -3/4 web-post-bl)
                                        ; (thumb-place 2 -3/4 web-post-br)
         )

   (hull (bottom-hull(thumb-place (+ 1/2 0.05) thumb-front-row-sides (translate [0 2.4 1]  bottom-wall-sphere-bottom-front)))
         (bottom-hull(thumb-place (+ 3/2 0.05) thumb-front-row-sides (translate [0 2.4 1]  bottom-wall-sphere-bottom-front)))
                                        ; (thumb-place 1 -5/8 web-post-bl)
                                        ; (thumb-place 1 -5/8 web-post-br)
                                        ; (thumb-place 2 -3/4 web-post-br)
         )

   (hull (bottom-hull(thumb-place thumb-right-wall thumb-front-row-sides (translate [-1 2.4 1]  bottom-wall-sphere-bottom-front)))
         (bottom-hull (thumb-place (+ 1/2 0.05) thumb-front-row-sides (translate [0 2.4 1]  bottom-wall-sphere-bottom-front)))
                                        ; (thumb-place 0 -1/2 web-post-bl)
                                        ; (thumb-place 1 -5/8 web-post-br)
                                        ; (thumb-place 0 -1/2 web-post-br)
         )])


(def thumb-back-wall-lower [(hull
                             (bottom-hull   (thumb-place 0.55 thumb-back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)));;back right area.  Connects to main case
                             (bottom-hull   (thumb-place 3/2 thumb-back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))
                             )

                            (hull
                             (bottom-hull  (thumb-place (+ 5/2 0.05) thumb-back-y (translate [2 -1 3]  bottom-wall-sphere-bottom-back)))
                             (bottom-hull    (thumb-place 3/2 thumb-back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))
                             )
                            (hull
                             #_(bottom-hull (thumb-place (+ 5/2 0.05) thumb-back-y (translate [1 -1 1]  bottom-wall-sphere-bottom-back)))
                             )
                            (hull
                             (bottom-hull  (thumb-place 0.55 thumb-back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))

                             (bottom-hull  (case-place left-wall-column 1.6666 (translate [1 0 1]  bottom-wall-sphere-bottom-front)));;not needed?
                             )
                            ])

(def  thumb-left-wall-lower [
                             (hull ;;top section
                              (bottom-hull   (thumb-place thumb-left-wall-column thumb-back-y (translate [2 -1 3]  bottom-wall-sphere-bottom-back)));;old [2 -1 1]
                              (bottom-hull (thumb-place thumb-left-wall-column 0 (translate [2 0 2]  bottom-wall-sphere-bottom-back))) )
                             (hull
                              (bottom-hull (thumb-place thumb-left-wall-column 0 (translate [2 0 2]  bottom-wall-sphere-bottom-back)))  )
                             (hull
                              (bottom-hull (thumb-place thumb-left-wall-column 0 (translate [2 0 2]  bottom-wall-sphere-bottom-back)))
                              (bottom-hull   (thumb-place thumb-left-wall-column -1 (translate [2 0 2]  bottom-wall-sphere-bottom-back)))
                              )
                             (hull
                              (bottom-hull  (thumb-place thumb-left-wall-column -1 (translate [2 0 2] bottom-wall-sphere-bottom-back)))   )
                             (hull;;bottom section
                              (bottom-hull    (thumb-place thumb-left-wall-column -1 (translate [2 0 2]  bottom-wall-sphere-bottom-back)))
                              (bottom-hull   (thumb-place thumb-left-wall-column (+ -1 0.15) (translate [2 1 1]  bottom-wall-sphere-bottom-front))))
                                        ; (thumb-place 2 -1 web-post-tl)
                                        ; (thumb-place 2 -1 web-post-bl)
                             ])

(def thumb-inside-lower
  [(hull
    ;; Front of case
    (bottom-hull   (case-place (- 2 1/2) 4 (translate [0 1 1]  bottom-wall-sphere-bottom-front)))
    (bottom-hull (case-place 0.70 3.95 (translate [0 1 1]  bottom-wall-sphere-thumb-side)))
    ; (bottom-hull   (case-place 0.70 0.72 (translate [0 1 1]  bottom-wall-sphere-thumb-side)) )
    )
   ;; thumb cluster side (
   (hull
    (bottom-hull    (case-place 0.70 3.95 (translate [0 1 1]  bottom-wall-sphere-thumb-side)))
    (bottom-hull   (case-place 0.72 3.86 (translate [0 1 1]  bottom-wall-sphere-thumb-front)) )
    )
   (hull
    (bottom-hull    (thumb-place thumb-right-wall thumb-front-row-sides (translate [-1 2.4 1]  bottom-wall-sphere-bottom-front)))
    (bottom-hull   (case-place 0.72 3.87 (translate [0 1 1]  bottom-wall-sphere-thumb-front)) )
    ;(bottom-hull   (case-place 0.72 3.84 (translate [0 1 1]  bottom-wall-sphere-thumb-front)) )

    )
   ])


(def front-wall-lower
  (concat
   (for [x (range 2 5)]
     (union
      (hull (bottom-hull(case-place (- x 1/2) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))
            (bottom-hull  (case-place (+ x 1/2) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))
            ;; (case-place (+ x 1/2) 4 (translate [0 1 1] wall-sphere-bottom-front))
            )
      (hull (bottom-hull(case-place (- x 1/2) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))
            )))
   [(hull (bottom-hull(case-place (- right-wall-column 0.1) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))
          (bottom-hull(bottom-hull(case-place (- right-wall-column 1) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))))
    (hull (bottom-hull(case-place (+ 4 1/2) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))
          (bottom-hull   (case-place (- right-wall-column-bottom  0.05) 4 (translate [0 1 1] bottom-wall-sphere-bottom-front)))
          )]))

(def right-wall-lower (concat
                       (for [x (drop-last rows)]
                         (hull (bottom-hull(case-place  right-wall-column-bottom x (translate [-1 0 1] ( bottom-wall-sphere-bottom 1/2))))
                               ))
                       (for [x (drop-last rows)]
                         (hull (bottom-hull(case-place  right-wall-column-bottom x (translate [-1 0 1] ( bottom-wall-sphere-bottom 1/2))))
                               (bottom-hull(case-place right-wall-column-bottom  (inc x) (translate [-1 0 1] ( bottom-wall-sphere-bottom 1/2))))
                               ))
                       [(union
                         (hull(bottom-hull (case-place right-wall-column-bottom  (first rows) (translate [-1 0 1] ( bottom-wall-sphere-bottom 1/2))))
                              (bottom-hull (case-place right-wall-column-bottom  back-y (translate [-1 -1 1] ( bottom-wall-sphere-bottom 1))))
                                        ; (key-place 5 (first rows) web-post-tr)
                              )
                         (hull (bottom-hull(case-place right-wall-column-bottom  4 (translate [-1 0 1] ( bottom-wall-sphere-bottom 1/2))))
                               (bottom-hull(case-place (- right-wall-column-bottom  0.05) 4 (translate [0 1 1] ( bottom-wall-sphere-bottom 0))))
                               )
                         (hull (bottom-hull(case-place right-wall-column-bottom  4 (translate [-1 0 1] ( bottom-wall-sphere-bottom 1/2))))

                               ))])
  )
(def back-wall-lower
  (concat


   (if (== alpha_ergo_style 0) (for [x (range 0.05 right-wall-column-bottom 0.985)]
                                 (union
                                  (hull
                                   (bottom-hull(case-place (- x 1/2) back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))
                                   (bottom-hull (case-place (+ x 1/2) back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))
                                   )
                                  )
                                 ))
   (if (== alpha_ergo_style 1)
     (for [x (range 0.05 right-wall-column-bottom 0.9)]
       (union
        (hull
         (bottom-hull(case-place (- x 1/2) back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))
         (bottom-hull (case-place (+ x 1/2) back-y (translate [0 -1 1]  bottom-wall-sphere-bottom-back)))
         )
        )
       ))

   )
  )

(def left-wall-lightcycle
  (let [place case-place]
    [(hull (bottom-hull(place left-wall-column 0.8 (translate [1 -1 1]  bottom-wall-sphere-bottom-back)))
           (bottom-hull (place left-wall-column 2 (translate [1 0 1]  bottom-wall-sphere-bottom-back)))
           )
     #_      (hull ;(place left-wall-column 1 (translate [1 0 1]  bottom-wall-sphere-bottom-back))
              (bottom-hull  (place left-wall-column 2 (translate [1 0 1]  bottom-wall-sphere-bottom-back)))
              )
     (hull (bottom-hull(place left-wall-column 2 (translate [1 0 1]  bottom-wall-sphere-bottom-back)))
           (bottom-hull(place left-wall-column 1.6666  (translate [1 0 1]  bottom-wall-sphere-bottom-front)))
           )
     (hull (bottom-hull(place left-wall-column 1.6666  (translate [1 0 1]  bottom-wall-sphere-bottom-front)))
           )]))


(def left-wall-dactyl
  (let [place case-place]
    [(hull  (bottom-hull(place left-wall-column -0.18 (translate [1 -1 1] bottom-wall-sphere-bottom-back)))
            (bottom-hull(place left-wall-column 1 (translate [1 0 1] bottom-wall-sphere-bottom-back))))
     (hull  (bottom-hull(place left-wall-column 1 (translate [1 0 1] bottom-wall-sphere-bottom-back)))
            (bottom-hull(place left-wall-column 2 (translate [1 0 1] bottom-wall-sphere-bottom-back))))
     (hull  (bottom-hull(place left-wall-column 2 (translate [1 0 1] bottom-wall-sphere-bottom-back)))
            (bottom-hull(place left-wall-column 1.6666  (translate [1 0 1] bottom-wall-sphere-bottom-front))))
     (hull  (bottom-hull(place left-wall-column 1.6666  (translate [1 0 1]  bottom-wall-sphere-bottom-front))))])
  )

(def left-wall-lower
  (if (== row-size 0) left-wall-dactyl
      (if (== row-size 1) left-wall-lightcycle))
  )

(def bottom-sides
  (union
   (let [shift #(translate [0 0 (+ (- web-thickness) -1)] %)
         web-post-tl (shift web-post-tl)
         web-post-tr (shift web-post-tr)
         web-post-br (shift web-post-br)
         web-post-bl (shift web-post-bl)
         half-shift-correction #(translate [0 (/ mount-height 2) 0] %)
         half-post-br (half-shift-correction web-post-br)
         half-post-bl (half-shift-correction web-post-bl)
         row-connections (concat
                          (for [column (drop-last columns)
                                row (drop-last rows)
                                :when (or (not= column 0)
                                          (not= row 4))]
                            (triangle-hulls
                             (key-place (inc column) row web-post-tl)
                             (key-place column row web-post-tr)
                             (key-place (inc column) row web-post-bl)
                             (key-place column row web-post-br)))
                          (for [column (drop-last columns)
                                row [(last rows)]
                                :when (or (not= column 0)
                                          (not= row 4))]
                            (triangle-hulls
                             (key-place (inc column) row web-post-tl)
                             (key-place column row web-post-tr)
                             (key-place (inc column) row half-post-bl)
                             (key-place column row half-post-br))))
         column-connections (for [column columns
                                  row (drop-last rows)
                                  :when (or (not= column 0)
                                            (not= row 3))]
                              (triangle-hulls
                               (key-place column row web-post-bl)
                               (key-place column row web-post-br)
                               (key-place column (inc row) web-post-tl)
                               (key-place column (inc row) web-post-tr)))
         diagonal-connections (for [column (drop-last columns)
                                    row (drop-last rows)
                                    :when (or (not= column 0)
                                              (not= row 3))]
                                (triangle-hulls
                                 (key-place column row web-post-br)
                                 (key-place column (inc row) web-post-tr)
                                 (key-place (inc column) row web-post-bl)
                                 (key-place (inc column) (inc row) web-post-tl)))
         bottom-keys  (for [column columns
                            row (drop-last rows) ;;
                            :when (or (not= column 0)
                                      (not= row 4))]
                        (bottom-hull   (->> bottom-key-guard
                                            (key-place column row))))
         bottom-keys2 (for [column columns
                            row [(last rows)] ;;
                            :when (or (not= column 0)
                                      (not= row 4))]
                        (bottom-hull   (->> bottom-front-key-guard
                                            (key-place column row))) )
         main-keys-bottom (concat row-connections

                                  column-connections
                                  diagonal-connections)

         #_(union
            (thumb-place 0 -1/2 shape)

            (thumb-place 1 7/8 shape)
            (thumb-place 1 -5/8 shape)

            (thumb-place 2 -3/4 shape)
            (thumb-place 2 3/4 shape)
            )


;; thumb-tl #(->> web-post-tl (translate [0 (extended-plate-height %) 0]))
;; thumb-bl #(->> web-post-bl (translate [0 (- (extended-plate-height %)) 0]))
;; thumb-tr #(->> web-post-tr (translate [0 (extended-plate-height %) 0]))
;; thumb-br #(->> web-post-br (translate [0 (- (extended-plate-height %)) 0]))

         thumbs [(triangle-hulls (thumb-place 0 -1/2 web-post-tl)
                                 (thumb-place 0 -1/2 web-post-bl)
                                 (thumb-place 1 -5/8 web-post-tr)
                                 (thumb-place 1 -5/8 web-post-br))
                 (hull (thumb-place 1 -5/8 web-post-tr)
                       (thumb-place 1 -5/8 web-post-tl)
                       (thumb-place 1 7/8 web-post-bl)
                       (thumb-place 1 7/8 web-post-br))
                 (hull (thumb-place 2 -3/4 web-post-tr)
                       (thumb-place 2 -3/4 web-post-tl)
                       (thumb-place 2 3/4 web-post-bl)
                       (thumb-place 2 3/4 web-post-br))
                 (triangle-hulls (thumb-place 2 3/4 web-post-tr)
                                 (thumb-place 1 7/8 web-post-tl)
                                 (thumb-place 2 3/4 web-post-br)
                                 (thumb-place 1 7/8 web-post-bl)
                                 (thumb-place 2 -3/4 web-post-tr)
                                 (thumb-place 1 -5/8 web-post-tl)
                                 (thumb-place 2 -3/4 web-post-br)
                                 (thumb-place 1 -5/8 web-post-bl)
                                 )]

         ]
     ( difference
      (cube 200 200 60)
      (translate [0 0 33])

      (concat
       bottom-keys
       ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; This is where to toggle whats in case bottom ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

     (apply union
            (concat
;; main-keys-bottom;turns off the connectors bottom-keys bottom-keys2
             front-wall-lower
             right-wall-lower
             back-wall-lower
             left-wall-lower
; (if (== row-size 0) left-wall-dactyl
; (if (== row-size 1) left-wall-lightcycle))
;  thumbs;turns off the connectors
             thumb-back-wall-lower
             thumb-left-wall-lower
             thumb-front-wall-lower
             thumb-inside-lower
;  stands
             )

            ))))
; (projection(cut = true) bottom-sides)


;;;;;;;;;;;;;;;;;;;;
;;; screw mounts ;;;
;;;;;;;;;;;;;;;;;;;;

;; Defines the locations of the mounts for top to bottom case
(def ergo_screw_x1 (+ 4 7/10) )       ;; Right side mounts
(def ergo_screw_y1 (+ row-size 1/5) ) ;; top mount position
(def ergo_screw_y2 (+ 3 8/10))


(def screw_x1 (+ 4 5/10))
(def screw_y1  (+ row-size 1/2))
(def screw_y2 (+ 3 1/2))

(def screw_x3 1/2)    ;; default 0.5 --top left mount x position
(def screw_y3 0.5)    ;; default 1.5 ---top left mount y position---try .6
(def screw_y31 0.5)   ;; default 1.5 ---top left mount y position---try .6

(def screw-hole (->> (cylinder 1.5 20)
                     (translate [0 0 3/2])
                     (with-fn wall-sphere-n)))


(def screw-hole1 (->> (if (< left-right-thumb-tilt -5)
                        (->>(cylinder 1.5 20)
                            (translate [0 0 3/2])
                            (rotate (/ (* π (/ left-right-thumb-tilt 4)) 180) [0 1 0])
                            (rotate (/ π 0.9) [1 0 0 ])
                            (with-fn wall-sphere-n)))

                      (if (> left-right-thumb-tilt -5)
                        (union (->> (cylinder 2.3 10)
                                    (translate [0 0 -8.])
                                    (rotate (/ π 10) [0 1 0])
                                    (rotate (/ π 1.05) [1 0 0 ])
                                    (with-fn wall-sphere-n) );(translate [0 0 5]))
                               (->> (cylinder 1.5 20)
                                    (translate [0 0 3/2])
                                    (rotate (/ π 10) [0 1 0])
                                    (rotate (/ π 1.05) [1 0 0 ])
                                    (with-fn wall-sphere-n))))

                      ))

(def screw-holes
  (union
   ;; (key-place screw_x3 screw_y31 screw-hole) ;;test remove

   (if (== alpha_ergo_style 1)
     (key-place ergo_screw_x1 ergo_screw_y1 screw-hole))
   (if (== alpha_ergo_style 1)
     (key-place ergo_screw_x1 ergo_screw_y2 screw-hole))
   (if (== alpha_ergo_style 0)
     (key-place screw_x1 screw_y1 screw-hole))
   (if (== alpha_ergo_style 0)
     (key-place screw_x1 screw_y2 screw-hole))

   (key-place screw_x3 screw_y3 screw-hole)


   (if (> thumb-type 0)  (thumb-place 3/2 -1/2 screw-hole1)  )
   (if (== thumb-type 0) (thumb-place 1.65 -0.2 screw-hole1))
   ))

;; Defines size of the mount square
(def screw-mounts-square (->>
                          (cube 10 10 26)
                          (translate [0 0 -17])
                          ))

(def screw-mounts-square1 (->>
                           (cube 10 10 46)
                           (translate [0 0 -25])
                           ))

(def screw-mounts-square2 (->>
                           (if (< left-right-thumb-tilt -5)
                             (->>(cube 10 10 50)
                                 (translate [0 0 30.])
                                 (rotate (/ (* π (/ left-right-thumb-tilt 4)) 180) [0 1 0])
                                 (rotate (/ π 0.9) [1 0 0 ])
                                 )

                             (if (> left-right-thumb-tilt -5) (->>
                                                               (cube 10 10 50)
                                                               (translate [0 0 26])
                                                               (rotate (/ π 10) [0 1 0])
                                                               (rotate (/ π 1.05) [1 0 0 ])
                                                               )))
                           ))
;; creates the screw holes based on the above
(def screw-mount
  (union
   ;; (key-place screw_x3 screw_y31 screw-mounts-square1)  ;;test remove
   (if (== alpha_ergo_style 1)
     (key-place ergo_screw_x1 ergo_screw_y1  screw-mounts-square))
   (if (== alpha_ergo_style 1)
     (key-place ergo_screw_x1 ergo_screw_y2  screw-mounts-square))
   (if (== alpha_ergo_style 0)
     (key-place screw_x1 screw_y1  screw-mounts-square))
   (if (== alpha_ergo_style 0)
     (key-place screw_x1 screw_y2  screw-mounts-square))

   (key-place screw_x3 screw_y3 screw-mounts-square1)
   (if (> thumb-type 0) (thumb-place 3/2 -1/2 screw-mounts-square2)  )
   (if (== thumb-type 0) (thumb-place  1.65 -0.2 screw-mounts-square2)
       )
   )
  )

(def screw-cutout
  (->>
   (union
    (->> (cylinder 1.75 65)
         (translate [0 0 3/2])
         (with-fn wall-sphere-n))
    (->> (cube 7 7 32)
         (translate [0 0 -24]))))
  )

(def screw-cutout1
  (->>
   (union
    (->> (cylinder 1.75 65)
         (translate [0 0 3/2])
         (with-fn wall-sphere-n))
    (->> (cube 7 7 50)
         (translate [0 0 -29])))
   )
  )
;; creates the hole in the mount for the screw to go through
(def screw-cutout2
  (if (< left-right-thumb-tilt -5)
    (->>  (union
           (->> (cylinder 1.75 65)
                (translate [0 0 3/2])
                (with-fn wall-sphere-n))
           (->> (cube 7 7 40)
                (translate [0 0 29])))
          (rotate (/ (* π (/ left-right-thumb-tilt 4)) 180) [0 1 0])
          (rotate (/ π 0.9) [1 0 0 ])
          )
    (if (> left-right-thumb-tilt -5)
      (->> (union
            (->> (cylinder 1.75 65)
                 (translate [0 0 3/2])
                 (with-fn wall-sphere-n))
            (->> (cube 7 7 40)
                 (translate [0 0 29])))
           (rotate (/ π 10) [0 1 0])
           (rotate (/ π 1.05) [1 0 0 ])
           )
      )
    )
  )

(def mount-cutouts
  (union
   ;; (key-place screw_x3 screw_y31 screw-cutout1) ;;test remove

   (if (== alpha_ergo_style 1)
     (key-place ergo_screw_x1 ergo_screw_y1  screw-cutout))
   (if (== alpha_ergo_style 1)
     (key-place ergo_screw_x1 ergo_screw_y2  screw-cutout))
   (if (== alpha_ergo_style 0)
     (key-place screw_x1 screw_y1  screw-cutout))
   (if (== alpha_ergo_style 0)
     (key-place screw_x1 screw_y2  screw-cutout))


   (key-place screw_x3 screw_y3 screw-cutout1)

   (if (> thumb-type 0) (thumb-place 3/2 -1/2  screw-cutout2)    )
   (if (== thumb-type 0) (thumb-place  1.65 -0.2   screw-cutout2)  )
   )
  )



(def heat-set-cutout
  (->>
   (union
    (->> (cylinder 1.75 65)
         (translate [0 0 3/2])
         (with-fn wall-sphere-n))
    (->> (cylinder 2.40 12)
         (translate [0 0 -5])
         )))
  )

(def heat-set-cutout1
  (->>
   (union
    (->> (cylinder 1.75 65)
         (translate [0 0 3/2])
         (with-fn wall-sphere-n))
    (->> (cylinder 2.40 12)
         (translate [0 0 -5])))
   )
  )


(def heat-set-cutout2
  (if (< left-right-thumb-tilt -5)
    (->>  (union
           (->> (cylinder 1.75 65)
                (translate [0 0 3/2])
                (with-fn wall-sphere-n))
           (->> (cylinder 2.40 12)
                (translate [0 0 7])
                ))
          (rotate (/ (* π (/ left-right-thumb-tilt 4)) 180) [0 1 0])
          (rotate (/ π 0.9) [1 0 0 ])
          )
    (if (> left-right-thumb-tilt -5)
      (->> (union
            (->> (cylinder 1.75 65)
                 (translate [0 0 15])
                 (with-fn wall-sphere-n))
            (->> (cylinder 2.40 12)
                 (translate [0 0 6])))
           (rotate (/ π 10) [0 1 0])
           (rotate (/ π 1.05) [1 0 0 ])
           )
      )
    )
  )


(def heat-set-mounts
  (difference
   (union
    ;; (key-place screw_x3 screw_y31 heat-set-cutout1) ;;test remove


    (if (== alpha_ergo_style 1)
      (key-place ergo_screw_x1 ergo_screw_y1  heat-set-cutout))
    (if (== alpha_ergo_style 1)
      (key-place ergo_screw_x1 ergo_screw_y2  heat-set-cutout))
    (if (== alpha_ergo_style 0)
      (key-place screw_x1 screw_y1  heat-set-cutout))
    (if (== alpha_ergo_style 0)
      (key-place screw_x1 screw_y2  heat-set-cutout))

    (key-place screw_x3 screw_y3 heat-set-cutout1)

    (if (> thumb-type 0) (thumb-place 3/2 -1/2  heat-set-cutout2)   )
    (if (== thumb-type 0) (thumb-place  1.65 -0.2   heat-set-cutout2) )
    )
   (cube 200 200 3.5)
   )

  )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; pro micro/trrs holder ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def usb-holder-position [0 0 2])
(def pro-micro-position [-0.5 (- 37 (* row-size 20)) 4]) ;; (def pro-micro-position [-18 16 4])
(def usb-holder-size [18.7 31  5.5 ])
(def usb-hole-size [18.6 33.98  9.5])
(def usb-hole-front [ 8.0 10.6 9.5])
(def usb-hole-back [10.6 35.6 6])
(def usb-holder-thickness 5.5)
(def usb-holder
  (->> (difference
        (cube (+ (first usb-holder-size) usb-holder-thickness) (+ (second usb-holder-size) usb-holder-thickness) (+ (last usb-holder-size) usb-holder-thickness) )
        (translate usb-holder-position)
        ;; (cube 5 5 5)
        ) (translate  pro-micro-position) ;; (apply cube usb-hole-size))
       ))
(def usb-holder-hole
  (->>
   (union
    (->>(apply cube usb-hole-size)
        (translate [(+ (first pro-micro-position) 0) (second pro-micro-position) (/ (+ (last usb-holder-size) -1) 1)]))
    (->>(apply cube usb-hole-front)
        (translate [(+ (first pro-micro-position) 1) (- (second pro-micro-position) 18) (+ (/ (last usb-holder-size) 2) (last usb-holder-position)) ]))
    (->>(apply cube usb-hole-back)
        (translate [(- (first pro-micro-position ) 0) (+ (second pro-micro-position) 10) (+ (/ (last usb-holder-size) 2) 2)]))
    )
   (translate usb-holder-position)
   ))

(def trrs-holder-position [-18.5 (- 47. (* row-size 20)) 5])
(def trrs-holder-size [12 13.6 9.2 ])
(def trrs-hole [10.7 12.7 11])
(def trrs-hole-front [7. 6. 8. ])
(def trrs-hole-back [3.35 10])
(def trrs-holder-thickness 4.0)
(def trrs-holder
  (->> (difference (cube (+ (first trrs-holder-size) trrs-holder-thickness) (+ (second trrs-holder-size) trrs-holder-thickness) (+ (last trrs-holder-size) trrs-holder-thickness)))
       (translate trrs-holder-position)
                                        ; (translate [(first trrs-holder-position) (second trrs-holder-position) (/ (+ (last trrs-holder-size) trrs-holder-thickness) 2)])
       ))
                                        ;(def trrs-holder
                                        ;   (->> (cube (+ (first usb-holder-size) usb-holder-thickness) (second usb-holder-size) (+ (last usb-holder-size) usb-holder-thickness))
                                        ;        (translate [(first usb-holder-position) (second usb-holder-position) (/ (+ (last usb-holder-size) usb-holder-thickness) 2)])))

(def trrs-holder-hole
  (->>
   (union
    (->>(apply cube trrs-hole)
        (translate [ (first trrs-holder-position) (second trrs-holder-position)  (+ (last trrs-holder-size) 0.8)]))

    (->>(apply cube trrs-hole-front)
        (translate [ (first trrs-holder-position) (- (second trrs-holder-position) 6.5) (+ (last trrs-holder-position) 3.5)]))

    (->>(apply   cylinder trrs-hole-back)
        (rotate 1.5708 [1 0 0])
        (translate [(first trrs-holder-position) (+ (second trrs-holder-position) 10)  (+ (last trrs-holder-position) 2) ])
        )
    ))
  )

(def test1   (hull (->> (cube 250 250 70)
                        (translate [0 0 38]) ;;half of height +the thickness of bottom
                        )))

(def test2  (->> (hull
                  (->> bottom-sides
                                        ;(cube 200 230 3)
                                        ;(translate [0 0 1.5]) ;;half of height +the thickness of bottom
                       ))
                 )
  )

(def pre-cut-bottom
  (difference
                                        ; bottom-sides
   test2
   test1
   )
  )

(def bottom-sides-moved
  (->> bottom-sides
       (translate[45 30 0])
       )
  )

;; not very efficient way to cut out the needed area to create the bottom part
;; of the case
(def plate-cutout
  (->>    (for [xyz (range 1.05 1.3 0.05)];controls the scale last number needs to be lower for thinner walls
            (union
             (scale[xyz, xyz,1] bottom-sides-moved)
                                        ;(translate [0 0 -3])
             )
            )
          (translate[-45 -30 0])
          )
  )


(def plate-cutout1
  (difference  (->>  (union  (for [xyz (range 1 12 1.7)];controls the scale last number needs to be lower for thinner walls
                               (->> (union
                                     (translate [(* -1 xyz) xyz 0] thumb-back-wall-lower)
                                     (translate [(* -1 xyz) xyz 0] left-wall-lower)

                                        ;  left-wall-lower
                                        ;(translate [0 0 -3])
                                     ))
                               )
                             (for [xyz (range 1 10 1.8)  ]
                               (->> (union
                                     (translate [(* -1 xyz) xyz 0] thumb-left-wall-lower)
                                     (translate [xyz (* -1 xyz) 0] front-wall-lower)
                                     (translate [xyz (* -1 xyz) 0] thumb-inside-lower)
                                     (translate [xyz (* -1 xyz) 0] thumb-front-wall-lower)))
                               )
                             )
                     )(translate [0 0 29] (cube 300 300 50)))
  )





(def bottom-plate1
  (difference
   (hull bottom-sides)
   test1
   )
  )




;;;;;;;;;;;;;;;;;;
;;; Wrist rest ;;;
;;;;;;;;;;;;;;;;;;

;; next 2 are not needed
#_(def wrist-rest-cut
    (->> (scale [1 1 2]
                (->> (scale [1 1 1] wrist-rest) (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0])
                     (translate [0 0 (+ 5 wrist-rest-back-height)]))
                )
         )
    )
#_(def wrist-rest-sides
    (->>
                                        ;(scale [2.5 2.5 1]
     (difference
      (->> (scale[1.1, 1.2, 1]
                                        ;(hull
                 (->> wrist-rest (rotate  (translate [0 0 wrist-rest-back-height])(/ (* π wrist-rest-angle) 180)  [1 0 0]) )
                 (->> wrist-rest (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0])))
           )
                                        ; (->> wrist-rest (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0])(translate [0 -2 (+ 2 wrist-rest-back-height)]))
      (->> wrist-rest-front-cut (rotate  (/ (* π wrist-rest-angle) 180)))
      )
                                        ;)
     )
    )
(def wrist-rest-front-cut

  (scale[1.1, 1, 1](->> (cylinder 7 200)(with-fn 300)
                        (translate [0 -13.4 0]))
                                        ;(->> (cube 18 10 15)(translate [0 -14.4 0]))
        ))

(def cut-bottom
  (->>(cube 200 200 100)(translate [0 0 -50]))
  )

(def h-offset
  (* (Math/tan(/ (* π wrist-rest-angle) 180)) 88)
  )

(def scale-cos
  (Math/cos(/ (* π wrist-rest-angle) 180))
  )

(def scale-amount
  (/ (* 83.7 scale-cos) 19.33)
  )

(def wrist-rest


  (scale [4.25  scale-amount  1] (difference (union
                                              (difference
                                        ;the main back circle
                                               (scale[1.3, 1, 1](->> (cylinder 10 150)(with-fn 200)
                                                                     (translate [0 0 0])))
                                        ;front cut cube and circle
                                               (scale[1.1, 1, 1](->> (cylinder 7 201)(with-fn 200)
                                                                     (translate [0 -13.4 0]))
                                                     (->> (cube 18 10 201)(translate [0 -12.4 0]))

                                                     ))
                                              ;;side fillers
                                              (->> (cylinder 6.8 200)(with-fn 200)
                                                   (translate [-6.15 -0.98 0]))
                                              (->> (cylinder 6.8 200)(with-fn 200)
                                                   (translate [6.15 -0.98 0]))
                                              ;;heart shapes at bottom
                                              (->> (cylinder 5.9 200)(with-fn 200)
                                                   (translate [-6.35 -2 0]))
                                              (scale[1.01, 1, 1](->> (cylinder 5.9 200)(with-fn 200)
                                                                     (translate [6.35 -2. 0])))
                                              )
                                             cut-bottom
                                             ))
  )

(def wrist-rest-base
  (->>
   (scale [1 1 1] ;;;;scale the wrist rest to the final size after it has been cut
          (difference
           (scale [1.08 1.08 1] wrist-rest )
           (->> (cube 200 200 200)(translate [0 0 (+ (+ (/ h-offset 2) (- wrist-rest-back-height h-offset) ) 100)]) (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0])(rotate  (/ (* π wrist-rest-y-angle) 180)  [0 1 0]))
                                        ; (->> (cube 200 200 200)(translate [0 0 (+ (+ (- wrist-rest-back-height h-offset) (* 2 h-offset)) 100)]) (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0]))
                                        ; (->> (cube 200 200 200)(translate [0 0 (+ (+ (/ (* 88 (Math/tan(/ (* π wrist-rest-angle) 180))) 4) 100) wrist-rest-back-height)]) (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0]))
           (->> (difference
                 wrist-rest
                 (->> (cube 200 200 200)(translate [0 0 (- (+ (/ h-offset 2) (- wrist-rest-back-height h-offset) ) (+ 100  wrist-rest-ledge))]) (rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0])(rotate  (/ (* π wrist-rest-y-angle) 180)  [0 1 0]))
                                        ;(->> (cube 200 200 200)(translate [0 0 (- (+ (/ (* 17.7 (Math/tan(/ (* π wrist-rest-angle) 180))) 4) wrist-rest-back-height)(+ 100  wrist-rest-ledge))])(rotate  (/ (* π wrist-rest-angle) 180)  [1 0 0])))
                 )
                )
           );(rotate  (/ (* π wrist-rest-rotation-angle) 180)  [0 0 1])
          ))
  )

(def rest-case-cuts
  (union
   ;;right cut
   (->> (cylinder 1.85 25)(with-fn 20) (rotate  (/  π 2)  [1 0 0])(translate [25 45 4.5]))
   (scale [1.3 1 1] (->> (cylinder 2 3.2)(with-fn 50) (rotate  (/  π 2)  [1 0 0])(translate [19.23 54 4.])))
   (->> (cube 6 3 12.2)(translate [25 39 1.5]))
   ;;middle cut
   (->> (cylinder 1.85 25)(with-fn 20) (rotate  (/  π 2)  [1 0 0])(translate [0 45 4.5]))
   (scale [1.3 1 1] (->> (cylinder 2 3.2)(with-fn 50) (rotate  (/  π 2)  [1 0 0])(translate [0 54 4.])))
   (->> (cube 6 3 12.2)(translate [0 39 1.5]))

   ;;left
   (->> (cylinder 1.85 25)(with-fn 20) (rotate  (/  π 2)  [1 0 0])(translate [-25 45 4.5]))
   (scale [1.3 1 1] (->> (cylinder 2 3.2)(with-fn 50) (rotate  (/  π 2)  [1 0 0])(translate [-19.23 54 4.])))
   (->> (cube 6 3 12.2)(translate [-25 39 1.5]))
   )
  )

(def rest-case-connectors
  (difference
   (union
    (scale [1 1 1.6] (->> (cylinder 6 60)(with-fn 200) (rotate  (/  π 2)  [1 0 0])(translate [25 13 0])))
                                        ; (->> (cube 5 3 8.5)(translate [20 42 0]))
    (scale [1 1 1.6] (->> (cylinder 6 60)(with-fn 200) (rotate  (/  π 2)  [1 0 0])(translate [0 13 0])))
    (scale [1 1 1.6] (->> (cylinder 6 60)(with-fn 200) (rotate  (/  π 2)  [1 0 0])(translate [-25 13 0])))
                                        ;rest-case-cuts
    )
   )
  )

(def wrist-rest-build
  (->> (difference
        (->> (union
                                        ;wrist-rest-sides
              (->> wrist-rest-base  (translate [(+ 0 rest-offset-x) (+ -15 rest-offset-y) 0])(rotate  (/ (* π wrist-rest-rotation-angle) 180)  [0 0 1]))
              (->> rest-case-connectors )
              ))
        cut-bottom
        rest-case-cuts
        ) (translate [20 -103 0]))
  )


(def text-insert
  (translate [0 0 2](extrude-linear {:height 4, :center true}
                                    (scale [1 1 1]
                                           (mirror [0 -1 0]
                                                   (translate [-47.4 22 3.7]
                                                              (scale [1 1 2] (union
                                        ;(text '"Liberation Sans" 2 '"3")
                                                                              (translate [0 0 0](text '"Liberation Sans" 22 case-text))
                                        ;(translate [6.5 -1.4 0](text '"Liberation Sans" 2 '"3"))
                                        ;(translate [7.5 0 0](text '"Liberation Sans" 2 '""))
                                                                              ))
                                                              )
                                                   )
                                           )
                                    ))
  )

(def test-text
  (extrude-linear {:height 8, :center true}
                  text-insert)

  )
(def trimmed_square_screw_mounts
  (->>  (difference
         screw-mount
         (->>(for [xyz (range 1.02 1.2 0.025)];controls the scale last number needs to be lower for thinner walls
               (union
                (scale[xyz, xyz,1] bottom-sides)
                                        ;(translate [0 0 -3])
                )
               ))
         ))
  )

(def bottom-case
  (scale[(- scale-bottom 0.0), scale-bottom, scale-bottom]
        (difference
         (union
          bottom-sides
          (translate [0 0 -1])
          (color [0 1 1])
          (difference
           (union
            usb-holder
            trrs-holder
            pre-cut-bottom
            ;;screw-mount
            trimmed_square_screw_mounts
            )
           plate-cutout1
           )
          )
         (if (== row-size 1) (->> (translate [0 0 0] usb-holder-hole)
                                  (translate [0 1.5 0] trrs-holder-hole)))
         (if (== row-size 0) (->>(translate [0 0 0] usb-holder-hole) (translate [0 0 0]trrs-holder-hole)))
                                        ;mount-cutouts ;;hollows out the screw mount squares and cuts holes in top
         heat-set-mounts

                                        ;dactyl-top-right;cuts the top of the case to ensure better mating surface
         cut-bottom
                                        ;(->> (cube 200 230 20)  (translate [0 0 -10]));;;cuts off everything below z=0
         (->> (cube 35 49.5 8) (rotate  (/ (* π 9) 180) [0 0 1])(translate [-74 15 0]))  ;;cleans up above thumb cluster
         (->> (cube 55 14 10) (translate [16 -72 0]))    ;;bottom cube to clean up ;;;normal build needs 49 10 10
         (if (== wrist-rest-on 1) (->> rest-case-cuts  (translate [20 -110 0]) );;add/remove the wrist rest holes
             (if (== wrist-rest-on 0) cut-bottom));
                                        ; ))
         )
        )
  )

;;;;;;;;;;;;;;;;;;;;
;;; Final Export ;;;
;;;;;;;;;;;;;;;;;;;;

(def dactyl-top-right
  (difference
   (union key-holes
          connectors
          thumb
          new-case
                                        ;]  caps
                                        ;thumbcaps
                                        ;screw-holes
          )
   (->> bottom-sides
        (translate [0 0 -1.6]))
   screw-holes
   )

  )

(def dactyl-top-left

  (->>  dactyl-top-right (mirror [1,0,0]))
  )
(def dactyl-bottom-right
  (union
   text-insert
   bottom-case

   )
  )

(def dactyl-bottom-left
  (union
   (->>  bottom-case (mirror [1,0,0]))
   (->> text-insert (translate [-22 0 0]))
                                        ;copyright
   )
  )


(def dactyl-wrist-rest-right
  (union
                                        ;dactyl-bottom-right
   wrist-rest-build
   )
  )

(def dactyl-wrist-rest-left
  (->> dactyl-wrist-rest-right (mirror[1,0,0]))
  )
                                        ; (def bottom-side-width 5)
(def pro-micro-trrs-mounts
  (difference
   (union
    usb-holder
    trrs-holder)
   (if (== row-size 1) (->> (translate [0 0 0] usb-holder-hole)
                            (translate [0 1.5 0] trrs-holder-hole)))
   (if (== row-size 0) (->>(translate [0 0 0] usb-holder-hole) (translate [0 0 0]trrs-holder-hole)))
   ))

(def side-sample
  (difference
   (union
    dactyl-bottom-right
    dactyl-top-right
    caps
    thumbcaps
    (if (== wrist-rest-on 1) (->> wrist-rest-build    )
        );;add/remove the wrist rest holes
    )
   )
  )

;; Shows only top with caps.
(def top-sample
  (difference
   (union
    dactyl-top-right
    caps
    thumbcaps)))

;; Shows only caps
(def keycaps
  (difference
   (union
    caps
    thumbcaps)))

(spit "things/dactyl-top-right.scad"
      (write-scad dactyl-top-right))

(spit "things/dactyl-bottom-right.scad"
      (write-scad dactyl-bottom-right))

(spit "things/dactyl-bottom-left.scad"
      (write-scad dactyl-bottom-left))

(spit "things/side-sample.scad"
      (write-scad side-sample))

(spit "things/top-sample.scad"
      (write-scad top-sample))

(spit "things/dactyl-top-left.scad"
      (write-scad dactyl-top-left))

(spit "things/dactyl-wrist-rest-right.scad"
      (write-scad dactyl-wrist-rest-right))

(spit "things/dactyl-wrist-rest-left.scad"
      (write-scad dactyl-wrist-rest-left))

(spit "things/pro-micro-trrs-mounts.scad"
      (write-scad pro-micro-trrs-mounts))

(spit "things/keycaps.scad"
      (write-scad keycaps))
