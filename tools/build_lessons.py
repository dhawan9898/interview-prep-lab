"""Regenerate every lesson JSON: base generators first, then the patch pass."""
import runpy, os
here = os.path.dirname(os.path.abspath(__file__))
os.chdir(here)
import sys; sys.path.insert(0, here)
for name in ('lessons_c', 'lessons_c2', 'lessons_dsa', 'lessons_dsa2', 'lessons_net1', 'lessons_net2'):
    runpy.run_path(os.path.join(here, name + '.py'), run_name='__main__')
runpy.run_path(os.path.join(here, 'lessons_patch.py'), run_name='__main__')
