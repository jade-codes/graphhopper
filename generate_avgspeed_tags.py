import xml.etree.ElementTree as ET
import re
import sys

def format_time(time_str):
    # Remove '@ (', ')', replace spaces and '-' with '_', remove ':'
    time_str = time_str.strip()
    time_str = time_str.replace('@ (', '').replace(')', '')
    time_str = re.sub(r'[\s\-]', '_', time_str)
    time_str = time_str.replace(':', '')
    return time_str

def process_conditional_tags(way, direction):
    tag_key = f"avgspeed:{direction}:conditional"
    tags = way.findall('tag')
    for idx, tag in enumerate(tags):
        if tag.attrib.get('k') == tag_key:
            value = tag.attrib['v']
            conditions = [c.strip() for c in value.split(';')]
            insert_idx = list(way).index(tag)
            for cond in conditions:
                m = re.match(r'([\d.]+)\s*@\s*\(([^)]+)\)', cond)
                if m:
                    speed, time = m.groups()
                    time_fmt = format_time(time)
                    new_tag = ET.Element('tag')
                    new_tag.set('k', f"avgspeed_{time_fmt}_{direction}")
                    new_tag.set('v', speed)
                    new_tag.tail = '\n'  # Add a line break after the tag
                    insert_idx += 1
                    way.insert(insert_idx, new_tag)

def main(input_filename, output_filename):
    tree = ET.parse(input_filename)
    root = tree.getroot()
    for way in root.findall('way'):
        process_conditional_tags(way, 'forward')
        process_conditional_tags(way, 'backward')
    tree.write(output_filename, encoding='utf-8', xml_declaration=True)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python generate_avgspeed_tags.py <input_osm_file> <output_osm_file>")
        sys.exit(1)
    main(sys.argv[1], sys.argv[2])
