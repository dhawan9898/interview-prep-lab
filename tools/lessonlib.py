import json, os
OUT = os.path.join(os.path.dirname(__file__), '..', 'app', 'src', 'main', 'assets', 'lessons')
def S(heading=None, body=None, bullets=None, code=None):
    d = {}
    if heading: d['heading'] = heading
    if body: d['body'] = body
    if bullets: d['bullets'] = bullets
    if code: d['code'] = code.strip('\n')
    return d
def SUB(title, summary, *sections): return {'title': title, 'summary': summary, 'sections': list(sections)}
def REF(label, url): return {'label': label, 'url': url}
def write(tid, summary, sections, subtopics, takeaways, refs):
    json.dump({'summary': summary, 'sections': sections, 'subtopics': subtopics, 'takeaways': takeaways, 'references': refs},
              open(os.path.join(OUT, tid + '.json'), 'w'), indent=2, ensure_ascii=False)
