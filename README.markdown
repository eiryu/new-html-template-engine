New HTML Template Engine（仮）
============================

# 作る理由

HTMLを楽に書くためのもの。nodeのJadeが似てる。
出来る限りタイプ数を少なくする。SHIFTキーも押したくない。

- 正しいHTMLであるかの検証はしない
- インデントが意味を持つ
- 一番最初に出現した要素がタグになる
- 属性は「名前:値」で記載
- テキストノードは「:値」というように記載。名前部分は空
- 値が空白を含む場合はシングルクオートで囲む
- 属性の区切りは1つ以上の半角スペース

## 入力

```
html
	head
		meta charset:utf8
		title :'hello world'
	body
		h1 :hello
```

## 出力

```
<html>
	<head>
		<meta charset="utf8">
		<title>hello world</title>
	</head>
	<body>
		<h1>hello</h1>
	</body>
</html>
```
