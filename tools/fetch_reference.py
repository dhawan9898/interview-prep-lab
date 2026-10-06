#!/usr/bin/env python3
"""Download the reference pages behind the lessons into reference-data/.

GfG and Wikipedia pages are converted to Markdown (article body only); RFCs are saved as the
official plain text. A manifest.json records the URL, status, size, and which lesson topics each
file backs. Re-run with --force to refetch; existing files are skipped otherwise.
"""
import argparse, datetime, html.parser, json, os, re, sys, time, urllib.error, urllib.request

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'reference-data')
UA = 'InterviewPrepLab-reference-fetch/1.0 (personal study project; contact via repo owner)'
DELAY = 1.5
G = 'https://www.geeksforgeeks.org/'

# (area, topic ids, url or [candidate urls])
GFG = [
 ('c', ['pointers'], G + 'c/c-pointers/'),
 ('c', ['memory-layout'], G + 'c/memory-layout-of-c-program/'),
 ('c', ['stack-vs-heap'], G + 'stack-vs-heap-memory-allocation/'),
 ('c', ['pointer-arithmetic'], G + 'c/pointer-arithmetics-in-c-with-examples/'),
 ('c', ['null-pointers'], G + 'c/null-pointer-in-c/'),
 ('c', ['function-pointers'], G + 'c/function-pointer-in-c/'),
 ('c', ['buffer-overflow'], G + 'c/buffer-overflow-attack-with-example/'),
 ('c', ['use-after-free'], G + 'c/dangling-void-null-wild-pointers/'),
 ('c', ['memory-leaks'], G + 'what-is-memory-leak-how-can-we-avoid/'),
 ('c', ['struct-padding'], G + 'structure-member-alignment-padding-and-data-packing/'),
 ('c', ['endianness'], G + 'little-and-big-endian-mystery/'),
 ('c', ['undefined-behavior'], G + 'undefined-behavior-c-cpp/'),
 ('c', ['arrays'], G + 'c/c-arrays/'),
 ('c', ['strings'], G + 'c/strings-in-c/'),
 ('c', ['dynamic-memory'], G + 'c/dynamic-memory-allocation-in-c-using-malloc-calloc-free-and-realloc/'),
 ('c', ['structs-unions'], G + 'c/structures-c/'),
 ('c', ['structs-unions'], G + 'c/union-c/'),
 ('c', ['bitwise'], G + 'c/bitwise-operators-in-c-cpp/'),
 ('c', ['storage-classes'], G + 'c/storage-classes-in-c/'),
 ('c', ['preprocessor'], G + 'c/cc-preprocessors/'),
 ('c', ['preprocessor'], G + 'c/macros-and-its-types-in-c-cpp/'),
 ('c', ['preprocessor'], G + 'c/c-define-preprocessor/'),
 ('c', ['file-io'], G + 'c/basics-file-handling-c/'),
 ('c', ['qualifiers'], G + 'c/const-qualifier-in-c/'),
 ('dsa/sorting', ['bubble-sort'], G + 'bubble-sort-algorithm/'),
 ('dsa/sorting', ['selection-sort'], G + 'selection-sort-algorithm-2/'),
 ('dsa/sorting', ['insertion-sort'], G + 'insertion-sort-algorithm/'),
 ('dsa/sorting', ['shell-sort'], G + 'shell-sort/'),
 ('dsa/sorting', ['merge-sort'], G + 'merge-sort/'),
 ('dsa/sorting', ['quick-sort'], G + 'quick-sort-algorithm/'),
 ('dsa/sorting', ['heap-sort'], G + 'heap-sort/'),
 ('dsa/sorting', ['counting-sort'], G + 'counting-sort/'),
 ('dsa/searching', ['linear-search'], G + 'linear-search/'),
 ('dsa/searching', ['binary-search'], G + 'binary-search/'),
 ('dsa/searching', ['jump-search'], G + 'jump-search/'),
 ('dsa/data-structures', ['stack'], G + 'stack-data-structure/'),
 ('dsa/data-structures', ['queue'], G + 'queue-data-structure/'),
 ('dsa/data-structures', ['circular-queue'], G + 'introduction-to-circular-queue/'),
 ('dsa/data-structures', ['linked-list'], G + 'singly-linked-list-tutorial/'),
 ('dsa/data-structures', ['doubly-linked-list'], G + 'doubly-linked-list/'),
 ('dsa/data-structures', ['hash-table'], G + 'hash-table-data-structure/'),
 ('dsa/data-structures', ['bst'], G + 'binary-search-tree-data-structure/'),
 ('dsa/data-structures', ['avl-tree'], G + 'introduction-to-avl-tree/'),
 ('dsa/data-structures', ['heap'], G + 'heap-data-structure/'),
 ('dsa/data-structures', ['trie'], G + 'trie-insert-and-search/'),
 ('dsa/data-structures', ['graph'], G + 'bfs-vs-dfs-binary-tree/'),
 ('dsa/data-structures', ['graph'], G + 'graph-and-its-representations/'),
 ('dsa/graph-algorithms', ['dijkstra'], G + 'dijkstras-shortest-path-algorithm-greedy-algo-7/'),
 ('dsa/graph-algorithms', ['mst'], G + 'kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/'),
 ('dsa/graph-algorithms', ['union-find'], G + 'introduction-to-disjoint-set-data-structure-or-union-find-algorithm/'),
 ('dsa/graph-algorithms', ['topological-sort'], G + 'topological-sorting/'),
 ('dsa/techniques', ['recursion'], G + 'introduction-to-recursion-2/'),
 ('dsa/techniques', ['dynamic-programming'], G + 'introduction-to-dynamic-programming-data-structures-and-algorithm-tutorials/'),
 ('dsa/techniques', ['two-pointers'], G + 'two-pointers-technique/'),
 ('dsa/techniques', ['kmp'], G + 'kmp-algorithm-for-pattern-searching/'),
]
# Hub pages above only index their articles; these are the lesson-relevant articles they link to.
_DS = 'dsa/data-structures'
_SUBPAGES = {
 'stack': ['introduction-to-stack-data-structure-and-algorithm-tutorials', 'applications-advantages-and-disadvantages-of-stack',
           'implement-stack-using-array', 'implement-a-stack-using-singly-linked-list', 'stack-implementation-using-deque',
           'convert-infix-expression-to-postfix-expression', 'check-for-balanced-parentheses-in-an-expression',
           'evaluation-of-postfix-expression', 'next-greater-element', 'the-stock-span-problem'],
 'queue': ['introduction-to-queue-data-structure-and-algorithm-tutorials', 'applications-advantages-and-disadvantages-of-queue',
           'basic-operations-for-queue-in-data-structure', 'introduction-and-array-implementation-of-queue',
           'queue-linked-list-implementation', 'queue-using-stacks', 'implement-stack-using-queue',
           'reversing-first-k-elements-queue', 'sliding-window-maximum-maximum-of-all-subarrays-of-size-k'],
 'heap': ['binary-heap', 'applications-of-heap-data-structure', 'how-to-check-if-a-given-array-represents-a-binary-heap',
          'kth-largest-element-in-an-array', 'heap-sort-for-decreasing-order-using-min-heap',
          'median-of-stream-of-integers-running-integers', 'merge-k-sorted-arrays', 'huffman-coding-greedy-algo-3'],
 'bst': ['introduction-to-binary-search-tree', 'applications-of-bst', 'insertion-in-binary-search-tree',
         'binary-search-tree-set-1-search-and-insertion', 'deletion-in-binary-search-tree', 'floor-and-ceil-from-a-bst',
         'inorder-successor-in-binary-search-tree', 'how-to-handle-duplicates-in-binary-search-tree',
         'a-program-to-check-if-a-binary-tree-is-bst-or-not', 'sorted-array-to-balanced-bst',
         'lowest-common-ancestor-in-a-binary-search-tree', 'find-k-th-smallest-element-in-bst-order-statistics-in-bst',
         'introduction-to-red-black-tree'],
 'linked-list': ['traversal-of-singly-linked-list', 'insert-a-node-at-front-beginning-of-a-linked-list',
                 'insert-node-at-the-end-of-a-linked-list', 'insert-a-node-at-a-specific-position-in-a-linked-list',
                 'remove-first-node-of-the-linked-list', 'remove-last-node-of-the-linked-list',
                 'delete-a-linked-list-node-at-a-given-position', 'search-an-element-in-a-linked-list-iterative-and-recursive',
                 'reverse-a-linked-list'],
}
for _topic, _slugs in _SUBPAGES.items():
    for _s in _slugs:
        GFG.append((_DS, [_topic], G + 'dsa/' + _s + '/'))

RFCS = [
 (826, ['arp']), (791, ['ipv4-header']), (8200, ['ipv6-header']), (4632, ['subnetting', 'lpm']),
 (792, ['icmp-traceroute']), (3022, ['nat']), (2328, ['ospf']), (4271, ['bgp']),
 (9293, ['tcp-handshake', 'tcp-teardown']), (1035, ['dns-resolution']), (2131, ['dhcp-lease']),
 (8446, ['tls-handshake']),
]
WIKI = [
 ('Ethernet_frame', ['ethernet']), ('IEEE_802.1Q', ['vlan']), ('Spanning_Tree_Protocol', ['stp']),
 ('Link_aggregation', ['lacp']), ('Network_switch', ['mac-learning', 'switch-diag']),
 ('Subnet', ['subnetting']), ('Longest_prefix_match', ['lpm']), ('Traceroute', ['icmp-traceroute']),
]

VOID = {'br', 'img', 'hr', 'meta', 'link', 'input', 'wbr', 'source', 'area', 'base', 'col', 'embed', 'param', 'track'}
SKIP_TAGS = {'script', 'style', 'noscript', 'svg', 'button', 'nav', 'footer', 'form', 'iframe', 'template'}
SKIP_CLASS = ('navbox', 'reflist', 'mw-editsection', 'toc', 'metadata', 'noprint', 'hatnote', 'sidebar',
              'reference', 'mw-empty-elt', 'printfooter', 'catlinks', 'vertical-navbox')


class MD(html.parser.HTMLParser):
    """Collects the Markdown of the subtree whose start tag satisfies `is_root`."""

    def __init__(self, is_root):
        super().__init__(convert_charrefs=True)
        self.is_root = is_root
        self.stack = []          # open tag names
        self.root_depth = None   # len(stack) when the root opened
        self.skip_depth = None
        self.out = []
        self.list_stack = []
        self.in_pre = False
        self.link_stack = []
        self.cell_row = []
        self.in_table = False
        self.table_rows = []
        self.cell = None

    # -- helpers
    @property
    def active(self):
        return self.root_depth is not None and self.skip_depth is None

    def emit(self, s):
        if self.active:
            self.out.append(s)

    def handle_starttag(self, tag, attrs):
        a = dict(attrs)
        cls = (a.get('class') or '')
        if self.root_depth is None and self.is_root(tag, a):
            self.root_depth = len(self.stack) + 1
        if tag not in VOID:
            self.stack.append(tag)
        if self.root_depth is None:
            return
        if self.skip_depth is None and (tag in SKIP_TAGS or any(c in cls for c in SKIP_CLASS)):
            if tag not in VOID:
                self.skip_depth = len(self.stack)
            return
        if self.skip_depth is not None:
            return
        if tag in ('h1', 'h2', 'h3', 'h4', 'h5', 'h6'):
            self.emit('\n\n' + '#' * int(tag[1]) + ' ')
        elif tag == 'p':
            self.emit('\n\n')
        elif tag == 'br':
            self.emit('\n')
        elif tag in ('ul', 'ol'):
            self.list_stack.append([tag, 0])
            self.emit('\n')
        elif tag == 'li':
            depth = max(0, len(self.list_stack) - 1)
            kind = self.list_stack[-1] if self.list_stack else ['ul', 0]
            kind[1] += 1
            marker = f"{kind[1]}." if kind[0] == 'ol' else '-'
            self.emit('\n' + '  ' * depth + marker + ' ')
        elif tag == 'pre':
            self.in_pre = True
            self.emit('\n\n```\n')
        elif tag == 'code' and not self.in_pre:
            self.emit('`')
        elif tag in ('strong', 'b'):
            self.emit('**')
        elif tag in ('em', 'i'):
            self.emit('*')
        elif tag == 'blockquote':
            self.emit('\n\n> ')
        elif tag == 'a':
            self.link_stack.append(a.get('href', ''))
        elif tag == 'img':
            alt = a.get('alt')
            if alt:
                self.emit(f'[image: {alt}]')
        elif tag == 'table':
            self.in_table = True
            self.table_rows = []
        elif tag == 'tr':
            self.cell_row = []
        elif tag in ('td', 'th'):
            self.cell = []
        elif tag == 'hr':
            self.emit('\n\n---\n')

    def handle_endtag(self, tag):
        if tag in VOID or tag not in self.stack:
            return
        # pop to the matching tag
        while self.stack:
            t = self.stack.pop()
            depth_after = len(self.stack)
            if self.skip_depth is not None and depth_after < self.skip_depth:
                self.skip_depth = None
            if self.root_depth is not None and depth_after < self.root_depth:
                self.root_depth = None
                self._done = True
            if t == tag:
                break
        if not self.active:
            return
        if tag in ('h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p'):
            self.emit('\n')
        elif tag in ('ul', 'ol'):
            if self.list_stack:
                self.list_stack.pop()
            self.emit('\n')
        elif tag == 'pre':
            self.in_pre = False
            self.emit('\n```\n')
        elif tag == 'code' and not self.in_pre:
            self.emit('`')
        elif tag in ('strong', 'b'):
            self.emit('**')
        elif tag in ('em', 'i'):
            self.emit('*')
        elif tag == 'a' and self.link_stack:
            self.link_stack.pop()
        elif tag in ('td', 'th') and self.cell is not None:
            self.cell_row.append(re.sub(r'\s+', ' ', ''.join(self.cell)).strip().replace('|', '/'))
            self.cell = None
        elif tag == 'tr' and self.cell_row:
            self.table_rows.append(self.cell_row)
            self.cell_row = []
        elif tag == 'table':
            self.in_table = False
            rows = [r for r in self.table_rows if any(r)]
            if rows:
                w = max(len(r) for r in rows)
                rows = [r + [''] * (w - len(r)) for r in rows]
                lines = ['| ' + ' | '.join(rows[0]) + ' |', '|' + '---|' * w]
                lines += ['| ' + ' | '.join(r) + ' |' for r in rows[1:]]
                self.emit('\n\n' + '\n'.join(lines) + '\n')
            self.table_rows = []

    def handle_data(self, data):
        if not self.active:
            return
        if self.cell is not None:
            self.cell.append(data)
            return
        if self.in_pre:
            self.out.append(data)
        else:
            self.out.append(re.sub(r'\s+', ' ', data))

    def markdown(self):
        s = ''.join(self.out)
        s = re.sub(r'[ \t]+\n', '\n', s)
        s = re.sub(r'\n{3,}', '\n\n', s)
        s = re.sub(r'\*\*\s*\*\*', '', s)
        # GfG wraps code in a language label and a stray backtick pair; drop both
        s = re.sub(r'(?m)^ ?(?:C\+\+|C#|C|Java|Python|JavaScript|Bash|Go|Scala|SQL|Shell|Text)\s+`\s*$', '', s)
        s = re.sub(r'(?m)^`\s*$', '', s)
        s = re.sub(r'(?m)^>\s*\n\n(?=\S)', '> ', s)
        s = re.sub(r'\n{3,}', '\n\n', s)
        return s.strip() + '\n'


def convert_gfg(raw):
    title = re.search(r'<h1[^>]*>(.*?)</h1>', raw, re.S)
    title = re.sub(r'<[^>]+>', '', title.group(1)).strip() if title else ''
    # article body = the first <div class="text"> that follows the article viewer container
    start = raw.find('article--viewer_content')
    sub = raw[start:] if start >= 0 else raw
    p = MD(lambda tag, a: tag == 'div' and (a.get('class') or '').split()[:1] == ['text'])
    p.feed(sub)
    body = p.markdown()
    return (f'# {title}\n\n' if title else '') + body


def convert_wiki(raw):
    title = re.search(r'<title>(.*?)</title>', raw, re.S)
    title = title.group(1).replace(' - Wikipedia', '').strip() if title else ''
    p = MD(lambda tag, a: tag == 'div' and a.get('id') == 'mw-content-text')
    p.feed(raw)
    return f'# {title}\n\n' + p.markdown()


def fetch(url):
    req = urllib.request.Request(url, headers={'User-Agent': UA, 'Accept-Language': 'en'})
    last = None
    for attempt in range(2):
        try:
            with urllib.request.urlopen(req, timeout=40) as r:
                return r.status, r.read().decode('utf-8', errors='replace')
        except urllib.error.HTTPError as e:
            return e.code, ''
        except Exception as e:  # network hiccup: retry once
            last = e
            time.sleep(3)
    return 0, str(last)


def slug(url):
    return re.sub(r'[^a-z0-9]+', '-', url.rstrip('/').split('/')[-1].lower()).strip('-')


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--force', action='store_true')
    ap.add_argument('--only', help='substring filter on url')
    ap.add_argument('--raw', action='store_true', help='also keep raw HTML next to the Markdown')
    args = ap.parse_args()

    os.makedirs(ROOT, exist_ok=True)
    mpath = os.path.join(ROOT, 'manifest.json')
    manifest = json.load(open(mpath)) if os.path.exists(mpath) else {}

    jobs = []
    for area, topics, urls in GFG:
        jobs.append(('gfg', area, topics, urls if isinstance(urls, list) else [urls]))
    for n, topics in RFCS:
        jobs.append(('rfc', 'networking/rfc', topics, [f'https://www.rfc-editor.org/rfc/rfc{n}.txt']))
    for page, topics in WIKI:
        jobs.append(('wiki', 'networking/wikipedia', topics, [f'https://en.wikipedia.org/wiki/{page}']))

    for kind, area, topics, urls in jobs:
        if args.only and not any(args.only in u for u in urls):
            continue
        for url in urls:
            name = slug(url)
            if kind == 'gfg':
                rel = os.path.join(area, topics[0], f'gfg-{name}.md')
            elif kind == 'rfc':
                rel = os.path.join(area, f'rfc{name.replace("rfc", "").replace("txt", "").strip("-")}.txt')
            else:
                rel = os.path.join(area, f'{name}.md')
            path = os.path.join(ROOT, rel)
            if os.path.exists(path) and not args.force and manifest.get(rel, {}).get('status') == 200:
                print('skip ', rel)
                break
            time.sleep(DELAY)
            status, raw = fetch(url)
            entry = {'url': url, 'kind': kind, 'topics': topics, 'status': status,
                     'fetched_at': datetime.datetime.now(datetime.timezone.utc).strftime('%Y-%m-%dT%H:%M:%SZ'),
                     'fetched_with': 'python urllib direct HTTP (Firecrawl MCP tools were not loaded in the session)'}
            if status != 200:
                print(f'FAIL {status} {url}')
                manifest.setdefault('_failed', {})[url] = status
                continue
            text = raw if kind == 'rfc' else (convert_gfg(raw) if kind == 'gfg' else convert_wiki(raw))
            os.makedirs(os.path.dirname(path), exist_ok=True)
            open(path, 'w', encoding='utf-8').write(text)
            if args.raw and kind != 'rfc':
                open(path + '.raw.html', 'w', encoding='utf-8').write(raw)
            entry.update({'bytes': len(text.encode()), 'words': len(text.split())})
            manifest[rel] = entry
            manifest.get('_failed', {}).pop(url, None)
            print(f'ok   {rel}  ({entry["words"]} words)')
            break  # first working candidate wins
        json.dump(manifest, open(mpath, 'w'), indent=2)


if __name__ == '__main__':
    main()
