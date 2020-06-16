# グラフィック用ミニ言語処理系

## 開発環境

* windows10
* java 11.0.2

## ミニ言語の文法

1. Prog
    -> Com_list [EOF]

2. Com_list
    -> [Rep_com Prim_com] Com_list?
3. Rep_com
    -> __{__ Com_list? [0-9]+ __}__
4. Prim_com
    -> ___walk___ [+-]?[0-9]+(.[0-9]+)? [+-]?[0-9]+(.[0-9]+)?
&emsp;| ___move___ [+-]?[0-9]+(.[0-9]+)? [+-]?[0-9]+(.[0-9]+)?
&emsp;| ___go_far___ [+-]?[0-9]+(.[0-9]+)?
&emsp;| ___move_far___ [+-]?[0-9]+(.[0-9]+)?
___太字___ は終端記号を示す。

### 文法3の動作(繰り返し命令)

繰り返し文は
`{ 繰り返しの対象の命令文(空白でも可) 繰り返し回数 }`
という動作である。また、入れ子構造をとる。

### 文法4の動作

* `walk a b`
現在置(極座標(r, θ, Φ))から直線を引きながら新しい位置(r, θ+a°, Φ+b°)に移動する。  

* `move a b`
現在置(極座標(r, θ, Φ))から直線は引かずに新しい位置(r, θ+a°, Φ+b°)に移動のみ行う。  

* `go_far d`
現在置(極座標(r, θ, Φ))から直線を引きながら新しい位置(r+d, θ, Φ)に移動する。

* `move_far d`
現在置(極座標(r, θ, Φ))から直線は引かずに新しい位置(r+d, θ, Φ)に移動のみ行う。

## 動作例

### sample1

#### 命令1

```
{ move_far 50
    { walk 1 20
    180 }
3 }
```

#### 実行結果1

<img src="https://user-images.githubusercontent.com/47876646/61169993-3559bc80-a59e-11e9-97bc-8ad4fd92e1e2.png" width="450px">

### sample2

#### 命令2

```
{ move_far 60
    { walk 1.5 2
        {
            { walk 10 20
            8 }
        8 }
    8 }
8 }
```

#### 実行結果2

<img src="https://user-images.githubusercontent.com/47876646/61169997-44d90580-a59e-11e9-8787-3dc4e82ec236.png" width="450px">

### sample3

#### 命令3

```
{ move_far 30
    { walk 1.8 3.6
        {
            { walk 3.6 3.6
            10 }
        10 }
    10 }
10 }
```

#### 実行結果3

<img src="https://user-images.githubusercontent.com/47876646/61170021-a7ca9c80-a59e-11e9-82bc-357a528348ed.png" width="450px">
