<script lang="ts">
  import type { NumericSequenceDefinition } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let namespace = "";
  let name = "";
  let start = 1;

  function onClick() {
    dispatch("create", { name, namespace, start });
    console.log(start);
  }

  $: valid = namespace.length > 0 && name.length > 0 && start !== undefined && start !== null;
</script>

<div class="wrapper">
  <span>
    <label for="name">Name:</label>
    <input id="name" type="text" bind:value={name} />
  </span>
  <span>
    <label for="namespace">Namespace:</label>
    <input id="namespace" type="text" bind:value={namespace} />
  </span>
  <span>
    <label for="start">Start:</label>
    <input id="start" type="number" bind:value={start} />
  </span>
  <button on:click={onClick} disabled={!valid}>Add</button>
</div>

<style>
  .wrapper {
    padding: 1em;
    margin-top: 1em;
    background-color: #eee;
    border-radius: 3px;
  }
</style>
